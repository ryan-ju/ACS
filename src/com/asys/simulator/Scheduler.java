/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.asys.simulator.exceptions.IdExistException;
import com.asys.simulator.exceptions.IdNotExistException;
import com.asys.simulator.exceptions.SchedulerEmptyException;

/**
 * @author ryan
 * 
 */
public class Scheduler {
	private GateFactory gf;
	private TransitionEventFactory tef;
	private EventList el;
	private GateEventListManager gelm;
	private static Scheduler instance;

	public static Scheduler getInstance() {
		if (instance == null) {
			instance = new Scheduler();
			instance.initialize();
		}
		return instance;
	}

	private Scheduler() {
		this.gf = GateFactory.getInstance();
		this.tef = TransitionEventFactory.getInstance();
		this.el = new EventList();
		this.gelm = new GateEventListManager();
	}

	protected void initialize() {
		gelm.initialize();
	}

	/**
	 * Return the next event from the scheduler, deleting it from the scheduler
	 * @return
	 * @throws IdNotExistException 
	 * @throws SchedulerEmptyException 
	 */
	public String getNextTransitionEventId() throws IdNotExistException, SchedulerEmptyException {
		if (isEmpty()) {
			throw new SchedulerEmptyException();
		} else {
			String next_event_id = el.removeFirst();
			TransitionEvent next_event = tef.getTransitionEvent(next_event_id);
			EventList gel = gelm.getEventList(next_event.getGateId());
			String next_event_id2 = gel.removeFirst();
			assert next_event_id.equals(next_event_id2);
			return next_event_id;
		}
	}
	
	/**
	 * Return the next event from the scheduler
	 * @return
	 * @throws IdNotExistException
	 * @throws SchedulerEmptyException
	 */
	public String getNextTransitionEventIdNoEffect() throws IdNotExistException, SchedulerEmptyException{
		if (isEmpty()) {
			throw new SchedulerEmptyException();
		} else {
			String next_event_id = el.getFirst();
			TransitionEvent next_event = tef.getTransitionEvent(next_event_id);
			EventList gel = gelm.getEventList(next_event.getGateId());
			String next_event_id2 = gel.getFirst();
			assert next_event_id.equals(next_event_id2);
			return next_event_id;
		}
	}

	/**
	 * For the moment, use the same scheduling scheme for both C and non-C gates.
	 * @param transition_event_id
	 * @return a list of consumed events that can be safely deleted from the TransitionEventFactory instance.
	 * @throws IdExistException 
	 * @throws IdNotExistException 
	 */
	public ArrayList<String> schedule(String transition_event_id)
			throws IdNotExistException, IdExistException {
		return scheduleEventNotForCGate(transition_event_id);
	}

	/**
	 * For non-C gates, their transition events are scheduled by the follow scheme:
	 * 
	 * If the event e1 to be scheduled is a start event, then all end events that are already scheduled 
	 * for the gate and are simultaneous with or later than e1 are removed.  If there is a start event
	 * immediately before e1, then e1 is STILL scheduled.  This is for checking common ambiguities.
	 * 
	 * If the event e2 to be scheduled is an end event, then it is added to the end of the events of 
	 * the gate.
	 * 
	 * There some important invariants about the list of scheduled transition events of a non-C gate:
	 * 1. There are not contiguous or simultaneous start events.
	 * 2. End events before start events must have been scheduled earlier than the start events.
	 * 3. Any newly start event scheduled must be the last start event in the queue.
	 * 4. Any newly end event scheduled must be the last end event in the queue.
	 * 
	 * @param transition_event_id
	 * @return an ArrayList of IDs of TransitionEvents that can be safely removed from transition event factory
	 * @throws IdNotExistException
	 * @throws IdExistException 
	 */
	private ArrayList<String> scheduleEventNotForCGate(
			String transition_event_id) throws IdNotExistException,
			IdExistException {
		// Assert that the transtion_event_id is NOT targeting a C element
		TransitionEvent event = tef.getTransitionEvent(transition_event_id);
		String gate_id = event.getGateId();
		// assert !(gate.getElement() instanceof CGate);

		ArrayList<String> waste = new ArrayList<String>();

		EventList gel = gelm.getEventList(gate_id);
		if (tef.isStartEvent(transition_event_id)) {
			/* 
			 * Remove all scheduled events for the gate after this start event.  
			 * By invariant 3, those removed can only be end events.
			 */
			List<String> useless_end_events = gel.removeEventsAtOrAfter(event
					.getCircuitTime());
			for (String id : useless_end_events) {
				waste.add(id);
			}
			el.removeEvents(useless_end_events);

			// Check if the previous event is a start event
			int i = gelm.getInsertionPosition(transition_event_id);
			String previous_event_id = gelm
					.getTransitionEventAt(gate_id, i - 1);
			if (previous_event_id != null
					&& tef.isStartEvent(previous_event_id)) {
				// This event is still scheduled
//				gel.insertEvent(transition_event_id);
//				el.insertEvent(transition_event_id);
			} else {// The previous event is not a start event
				gel.insertEvent(transition_event_id);
				el.insertEvent(transition_event_id);
			}
			return waste;
		} else {
			gel.insertEvent(transition_event_id);
			el.insertEvent(transition_event_id);
		}
		return waste;
	}

	protected EventList getEventListForGate(String gate_id) {
		if (gelm.hasKey(gate_id)) {
			try {
				return gelm.getEventList(gate_id);
			} catch (IdNotExistException e) {
				System.out.println("Should never have happened!");
				e.printStackTrace();
			}
			return null;
		} else {
			return null;
		}
	}

	protected int getTotalNumberOfEventsOnScheduler() {
		return el.getCurrentNumberOfEvents();
	}

	protected boolean isEmpty() {
		return el.getCurrentNumberOfEvents() <= 0;
	}

	// /**
	// * For C gates, their transition events are scheduled by the follow
	// scheme:
	// *
	// * To schedule a start event e1 on one input, firstly check the state of
	// the other input. If the
	// * other input has binary value, then
	// *
	// * @param transition_event_id
	// * @return
	// * @throws IdNotExistException
	// * @throws IdExistException
	// */
	// private ArrayList<String> scheduleEventForCGate(
	// String transition_event_id) throws IdNotExistException,
	// IdExistException {
	//
	//
	// }

	/**
	 * This class is for keeping track of the list of scheduled events.
	 * 
	 * @author ryan
	 * 
	 */
	private class EventList {
		private LinkedList<String> list;

		protected EventList() {
			list = new LinkedList<String>();
		}

		protected int getInsertionPosition(String event_id)
				throws IdExistException, IdNotExistException {
			if (list.contains(event_id)) {
				throw new IdExistException(event_id);
			} else {
				TransitionEvent new_event = tef.getTransitionEvent(event_id);
				assert new_event != null;
				int i = 0;
				for (String id : list) {
					TransitionEvent event = tef.getTransitionEvent(id);
					if (new_event.getCircuitTime() < event.getCircuitTime()
							|| (new_event.getCircuitTime() == event
									.getCircuitTime()
									&& tef.isStartEvent(event_id) && !tef
									.isStartEvent(id))) {
						break;
					}
					i++;
				}
				return i;
			}
		}

		/**
		 * Inserting an event_id into the list of event IDs.
		 * 
		 * The position of the inserted event_id depends on the time and type of the event inserted (start or end event).
		 * 
		 * The list is ordered by increasing circuit time.
		 * 
		 * If two events have the same circuit time, then start event has precedence over end event.
		 * 
		 * Otherwise, the first inserted event takes precedence.
		 * 
		 * @param event_id
		 * @return the index at which the new event ID is inserted.
		 * @throws IdExistException
		 * @throws IdNotExistException 
		 */
		protected int insertEvent(String event_id) throws IdExistException,
				IdNotExistException {
			int i = getInsertionPosition(event_id);
			list.add(i, event_id);
			return i;
		}

		protected String getFirst() {
			return list.getFirst();
		}

		protected String getLast() {
			return list.getLast();
		}

		protected String getAt(int i) {
			if (0 <= i && i < list.size()) {
				return list.get(i);
			} else {
				return null;
			}
		}

		protected String removeFirst() {
			return list.removeFirst();
		}

		protected String removeLast() {
			return list.removeLast();
		}

		protected boolean remove(String event_id) {
			return list.removeFirstOccurrence(event_id);
		}

		/**
		 * Return all event IDs stored.  It is important that the returned list is NOT modified in any way.
		 * @return
		 */
		protected LinkedList<String> getAllEvents() {
			return list;
		}

		/**
		 * Return an ArrayList of event IDs that are at or later than the time
		 * @param time
		 * @return
		 */
		protected List<String> getEventsAtOrAfter(long time) {
			int i = 0;
			for (String event_id : list) {
				TransitionEvent event = tef.getTransitionEvent(event_id);
				assert event != null;
				if (time <= event.getCircuitTime()) {
					break;
				}
				i++;
			}
			return list.subList(i, list.size());
		}

		/**
		 * Remove and return an ArrayList of event IDs that are at or later than the time
		 * @param time
		 * @return
		 */
		protected List<String> removeEventsAtOrAfter(long time) {
			int i = 0;
			for (String event_id : list) {
				TransitionEvent event = tef.getTransitionEvent(event_id);
				assert event != null;
				if (time <= event.getCircuitTime()) {
					break;
				}
				i++;
			}
			List<String> result = new LinkedList<String>();

			result.addAll(list.subList(i, list.size()));

			// Remove event IDs from the list
			int size = list.size();
			for (int j = i; j < size; j++) {
				list.remove(i);
			}

			return result;
		}

		protected void removeEvents(List<String> event_ids) {
			list.removeAll(event_ids);
		}

		protected int getCurrentNumberOfEvents() {
			return list.size();
		}
	}

	/**
	 * Used for tracking what events have been scheduled for each gate.
	 * @author ryan
	 * 
	 */
	private class GateEventListManager {
		private HashMap<String, EventList> map;

		protected GateEventListManager() {
			// Do nothing
		}

		protected void initialize() {
			this.map = new HashMap<String, EventList>();
			for (String gate_id : GateFactory.getInstance().getGateIds()) {
				map.put(gate_id, new EventList());
			}
		}

		protected boolean hasKey(String gate_id) {
			return map.containsKey(gate_id);
		}

		protected EventList getEventList(String gate_id)
				throws IdNotExistException {
			if (gf.gateIdExists(gate_id)) {
				return map.get(gate_id);
			} else {
				throw new IdNotExistException(gate_id);
			}
		}

		protected int getInsertionPosition(String transition_event_id)
				throws IdNotExistException, IdExistException {
			if (tef.transitionEventIdExists(transition_event_id)) {
				TransitionEvent transition_event = tef
						.getTransitionEvent(transition_event_id);
				String gate_id = transition_event.getGateId();
				return map.get(gate_id).getInsertionPosition(
						transition_event_id);
			} else {
				throw new IdNotExistException(transition_event_id);
			}
		}

		/**
		 * 
		 * @param transition_event_id
		 * @return the index at which the new event ID is inserted.
		 * @throws IdNotExistException
		 * @throws IdExistException
		 */
		protected int insertEvent(String transition_event_id)
				throws IdNotExistException, IdExistException {
			if (tef.transitionEventIdExists(transition_event_id)) {
				TransitionEvent transition_event = tef
						.getTransitionEvent(transition_event_id);
				String gate_id = transition_event.getGateId();
				return map.get(gate_id).insertEvent(transition_event_id);
			} else {
				throw new IdNotExistException(transition_event_id);
			}
		}

		protected String getTransitionEventAt(String gate_id, int i) {
			EventList list = map.get(gate_id);
			return list.getAt(i);
		}

		/**
		 * Return an ArrayList of event IDs that are at or later than the time
		 * @param gate_id
		 * @param time
		 * @return
		 */
		protected List<String> getTransitionEventsAtOrAfter(String gate_id,
				long time) {
			EventList list = map.get(gate_id);
			return list.getEventsAtOrAfter(time);
		}

		/**
		 * Remove and return an ArrayList of event IDs that are at or later than the time
		 * @param gate_id
		 * @param time
		 * @return
		 */
		protected List<String> removeTransitionEventsAfter(String gate_id,
				long time) {
			EventList list = map.get(gate_id);
			return list.removeEventsAtOrAfter(time);
		}

		protected void removeTransitionEvents(String gate_id,
				List<String> event_ids) {
			EventList list = map.get(gate_id);
			list.removeEvents(event_ids);
		}
	}
}
