/**
 * 
 */
package com.asys.simulator;

import java.awt.Event;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.asys.constants.Constant;
import com.asys.constants.LogicValue;
import com.asys.editor.model.CGate;
import com.asys.editor.model.Element;
import com.asys.editor.model.EnvironmentGate;
import com.asys.editor.model.OutputGate;
import com.asys.simulator.exceptions.IdExistException;
import com.asys.simulator.exceptions.IdNotExistException;
import com.asys.simulator.exceptions.SchedulerEmptyException;

/**
 * The processor takes events from scheduler, processes them, then put new events on the scheduler
 * 
 * @author ryan
 *
 */
public class EventProcessor {
	private Scheduler scheduler;
	private GateManager gm;
	private PortManager pm;
	private TransitionEventFactory tem;
	// The key is transition_event_id+input_port_id
	private HashMap<String, InputEvent> hard_set_map;
	private long purge_steps;
	private long purge_time_marker;
	private HashMap<String, LogicValue> local_input_cache;
	private static EventProcessor instance;

	public static EventProcessor getInstance() {
		if (instance == null) {
			instance = new EventProcessor();
		}
		return instance;
	}

	private EventProcessor() {
		this.scheduler = Scheduler.getInstance();
		this.gm = GateFactory.getInstance();
		this.pm = PortFactory.getInstance();
		this.tem = TransitionEventFactory.getInstance();
		this.hard_set_map = new HashMap<String, InputEvent>();
		this.purge_steps = 0;
		this.purge_time_marker = 0;
		local_input_cache = new HashMap<String, LogicValue>();
	}

	/**
	 * 
	 * @param transition_event_id
	 * @throws IdNotExistException 
	 * @throws IdExistException 
	 */
	public ArrayList<String> process(String transition_event_id) throws IdNotExistException,
			IdExistException {
		ArrayList<String> event_scheduled_id = new ArrayList<String>();
		local_input_cache.clear();
		
		TransitionEvent event = tem.getTransitionEvent(transition_event_id);
		String parent_id = event.getGateId();
		Gate parent = gm.getGate(parent_id);
		LogicValue old_input_value = parent.getCurrentLogicValue();
		System.out.println("\tGate \""+parent.getGateName()+"\" has old value "+old_input_value.toString());
		
		OutputPort op = pm.getOutputPort(parent.getOutputPortId());
		List<String> ip_ids = op.getInputPortIds();

		// Dish out the event to children
		for (String ip_id : ip_ids) {
			InputPort ip = pm.getInputPort(ip_id);
			String child_id = ip.getGateId();
			Gate child = gm.getGate(child_id);
			Element child_elt = child.getElement();

			if (child_elt instanceof CGate) {
				
				
				
				
			} else if (child_elt instanceof EnvironmentGate) {
				AbstractScript script = Queries.getScript(child_id);
				assert script != null;
				if (tem.isStartEvent(transition_event_id)) {
					AbstractScript.DelayValuePair pair = script.getNextPair();
					while (pair != null && pair.getLogicValue() != LogicValue.X){
						pair = script.getNextPair();
					}
					if (pair != null){
						LogicValue new_lv = pair.getLogicValue();
						long min_delay = pair.getDelay();
						assert new_lv == LogicValue.X;
						CausativeLink new_link = event.getCausativeLink().copy();
						String new_event_id = tem.createTransitionEvent(child_id, new_lv, event.getCircuitTime()+min_delay, event.getCircuitTime(), new_link);
						new_link.add(new_event_id);
						scheduler.schedule(new_event_id);
						event_scheduled_id.add(new_event_id);
					}
				}else{ // end event
					AbstractScript.DelayValuePair pair = script.getNextPair();
					while (pair != null && pair.getLogicValue() == LogicValue.X){
						pair = script.getNextPair();
					}
					if (pair != null){
						LogicValue new_lv = pair.getLogicValue();
						long max_delay = pair.getDelay();
						assert new_lv != LogicValue.X;
						CausativeLink new_link = event.getCausativeLink().copy();
						String new_event_id = tem.createTransitionEvent(child_id, new_lv, event.getCircuitTime()+max_delay, event.getCircuitTime(), new_link);
						new_link.add(new_event_id);
						scheduler.schedule(new_event_id);
						event_scheduled_id.add(new_event_id);
					}
				}
			} else if (child_elt instanceof OutputGate) {
				// Just set the new value, there is no need to consider delays
				child.setCurrentLogicValue(event.getNewValue());
			} else {

				// To schedule a new event, must first test if the driving
				// event has no effect.
				ArrayList<LogicValue> new_input_lvs = new ArrayList<LogicValue>();
				ArrayList<LogicValue> old_input_lvs = new ArrayList<LogicValue>();
				for (LogicValue lv:Queries.getOtherInputValues(local_input_cache, ip_id)){
					new_input_lvs.add(lv);
					old_input_lvs.add(lv);
				}
				new_input_lvs.add(event.getNewValue());
				old_input_lvs.add(old_input_value);
				EvaluationStrategy es = EvaluationStrategy
						.getEvaluationStrategy(child_elt);
				assert es != null;
				LogicValue new_lv = es.evaluate(new_input_lvs, child.getCurrentLogicValue());
				LogicValue old_lv = es.evaluate(old_input_lvs, child.getCurrentLogicValue());
				System.out.println("\tFor gate \""+child.getGateName()+"\", old_lv = "+old_lv.toString()+", new_lv = "+new_lv.toString());
				if (tem.isStartEvent(transition_event_id)) {
					assert event.getNewValue() == LogicValue.X;

					// Test if the transition has not effect, ie, new_lv != X
					if (new_lv == LogicValue.X && old_lv != LogicValue.X) { // Could have effect
						// Get the child gate's minimum delay
						long child_min_delay = Queries.getMinDelay(child_id);
						// Create a new CausativeLink
						CausativeLink new_link = event.getCausativeLink()
								.copy();
						String new_start_event_id = tem.createTransitionEvent(
								child_id, new_lv,
								child_min_delay + event.getCircuitTime(),
								event.getCircuitTime(), new_link);
						new_link.add(new_start_event_id);
						// Schedule the new event
						scheduler.schedule(new_start_event_id);
						event_scheduled_id.add(new_start_event_id);
					} else { // No effect
						assert new_lv != null;
						// Don't schedule any event
					}
				} else { // transition_event_id is an end event
					assert event.getNewValue() != LogicValue.X;
					// Test if the transition has not effect, ie, new_lv == X
					if (new_lv != LogicValue.X && old_lv == LogicValue.X) { // Could have effect
						// Get the child gate's minimum delay
						long child_max_delay = Queries.getMaxDelay(child_id);
						// Create a new CausativeLink
						CausativeLink new_link = event.getCausativeLink()
								.copy();
						String new_end_event_id = tem.createTransitionEvent(
								child_id, new_lv,
								child_max_delay + event.getCircuitTime(),
								event.getCircuitTime(), new_link);
						new_link.add(new_end_event_id);
						// Schedule the new event
						scheduler.schedule(new_end_event_id);
						event_scheduled_id.add(new_end_event_id);
					} else { // No effect
						assert new_lv != null;
						// Don't schedule any event
					}
				}
				// Update local_input_cache by adding the mapping
				local_input_cache.put(ip_id, event.getNewValue());
			}

		}
		// Set the parent's logic value
		parent.setCurrentLogicValue(event.getNewValue());
		
		purge_steps++;
		if (purge_steps >= Constant.PURGE_TRANSITION_EVENT_FACTORY_STEP_LIMIT) {
			try {
				purgeTransitionEventFactory();
			} catch (SchedulerEmptyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return event_scheduled_id;
	}
	
	

	private void purgeTransitionEventFactory() throws IdNotExistException, SchedulerEmptyException {
		tem.removeEventsAfter(purge_time_marker);
		purge_steps = 0;
		// Reset purge_time_marker to the latest event.
		purge_time_marker = tem.getTransitionEvent(scheduler.getNextTransitionEventIdNoEffect()).getCircuitTime();
	}

	// ====================================================
	// Private methods
	// ====================================================

	// ====================================================
	// Private classes
	// ====================================================
	private class InputEvent {
		final String transition_event_id;
		final String input_port_id;
		boolean hard_set;

		public InputEvent(String transition_event_id, String input_port_id) {
			this.transition_event_id = transition_event_id;
			this.input_port_id = input_port_id;
		}

		public boolean isHardSet() {
			return hard_set;
		}

		public void setIsHardSet(boolean b) {
			this.hard_set = b;
		}
	}
}
