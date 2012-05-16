/**
 * 
 */
package com.asys.simulator;

import java.util.HashMap;
import java.util.Map.Entry;

import com.asys.constants.LogicValue;
import com.asys.simulator.exceptions.IdNotExistException;

/**
 * @author ryan
 * 
 */
public class TransitionEventFactory implements TransitionEventManager {
	private static TransitionEventFactory instance;
	private final HashMap<String, TransitionEvent> event_map;
	private final HashMap<String, Transition> transition_map;
	private long transition_index;

	public static TransitionEventFactory getInstance() {
		if (instance == null) {
			instance = new TransitionEventFactory();
		}
		return instance;
	}

	private TransitionEventFactory() {
		this.event_map = new HashMap<String, TransitionEvent>();
		this.transition_map = new HashMap<String, Transition>();
		transition_index = 0;
	}

	public Transition getTransition(String id) {
		return transition_map.get(id);
	}

	public Transition removeTransition(String id) {
		Transition transition = transition_map.remove(id);
		if (transition != null) {
			String start_event_id = transition.getStartEventId();
			String end_event_id = transition.getEndEventId();
			event_map.remove(start_event_id);
			event_map.remove(end_event_id);
			return transition;
		} else {
			return null;
		}
	}
	
	public String createTransitionEvent(String gate_id, LogicValue new_value, long circuit_time, long real_time, CausativeLink causative_link){
		assert new_value != null;
		String type;
		CausativeLink link;
		if (new_value == LogicValue.X){
			type = "s";
		}else{
			type = "e";
		}
		String event_id = type+":"+"("+gate_id+"):"+circuit_time+":"+real_time+":"+transition_index;
		transition_index++;
		
		if (causative_link == null){
			link = new CausativeLink();
		}else{
			link = causative_link.copy();
		}
		
		TransitionEvent event = new TransitionEvent(event_id,
				circuit_time, real_time, gate_id, new_value,
				link, null);
		
		event_map.put(event_id, event);
		
		try {
			link.add(event_id);
		} catch (IdNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return event_id;
	}

//	/**
//	 * 
//	 * @param gate_id
//	 * @param new_value
//	 * @param min_delay
//	 * @param max_delay
//	 * @param causative_link
//	 * @return the ID of the transition created
//	 * @throws InvalidPropertyKeyException
//	 */
//	public String createTransition(String gate_id, LogicValue new_value,
//			long base_time, long real_time, long min_delay, long max_delay,
//			CausativeLink causative_link) throws InvalidPropertyKeyException {
//		assert min_delay < max_delay;
//		String start_event_id = "s:" + gate_id + ":" + (base_time + min_delay)
//				+ ":" + real_time + ":" + transition_index;
//		String end_event_id = "e:" + gate_id + ":" + (base_time + max_delay)
//				+ ":" + real_time + ":" + transition_index;
//		String transition_id = "t:" + gate_id + ":" + real_time + ":"
//				+ transition_index;
//		transition_index++;
//
//		// The CausativeLinks for start_event and end_event
//		CausativeLink link_start, link_end;
//
//		if (causative_link == null) {
//			link_start = new CausativeLink();
//			link_end = new CausativeLink();
//		} else {
//			link_start = causative_link.copy();
//			link_end = causative_link.copy();
//		}
//
//		// Create start_event, end_event and transition
//		TransitionEvent start_event = new TransitionEvent(start_event_id,
//				base_time + min_delay, real_time, gate_id, LogicValue.X,
//				link_start, transition_id);
//		TransitionEvent end_event = new TransitionEvent(end_event_id, base_time
//				+ max_delay, real_time, gate_id, new_value, link_end,
//				transition_id);
//		Transition transition = new Transition(transition_id, start_event_id,
//				end_event_id);
//
//		transition.setDirection(new_value == LogicValue.ONE ? true : false);
//
//		// Add start_event, end_event and transition to their respective maps.
//		event_map.put(start_event_id, start_event);
//		event_map.put(end_event_id, end_event);
//		transition_map.put(transition_id, transition);
//
//		try {
//			link_start.add(start_event_id);
//		} catch (IdNotExistException e) {
//			e.printStackTrace();
//		}
//
//		try {
//			link_end.add(end_event_id);
//		} catch (IdNotExistException e) {
//			e.printStackTrace();
//		}
//
//		return transition_id;
//	}

	@Override
	public TransitionEvent getTransitionEvent(String id) {
		return event_map.get(id);
	}

	@Override
	public boolean transitionEventIdExists(String id) {
		return event_map.containsKey(id);
	}

	@Override
	public boolean isStartEvent(String id) throws IdNotExistException {
		if (transitionEventIdExists(id)) {
			return id.substring(0, 1).equals("s");
		} else {
			throw new IdNotExistException(id);
		}
	}
	
	@Override
	public void removeEventsAfter(long time){
		for (Entry<String, TransitionEvent> entry:event_map.entrySet()){
			if (entry.getValue().getCircuitTime()<time){
				transition_map.remove(entry.getKey());
			}
		}
	}
}
