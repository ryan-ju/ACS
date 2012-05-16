/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.LinkedList;

import com.asys.constants.Constant;
import com.asys.simulator.exceptions.IdNotExistException;

/**
 * @author ryan
 * 
 */

public class CausativeLink {
	private LinkedList<String> transition_event_ids;
	private static TransitionEventManager transition_event_manager;
	private static final int limit = Constant.CAUSATIVE_LINK_LIMIT;

	public CausativeLink() {
		this.transition_event_ids = new LinkedList<String>();
		transition_event_manager = TransitionEventFactory.getInstance();
	}

	private CausativeLink(LinkedList<String> transition_event_ids) {
		this.transition_event_ids = transition_event_ids;
		transition_event_manager = TransitionEventFactory.getInstance();
	}

	/**
	 * Add an transition event ID to the end of this causative link. Discard the
	 * head if the size of this causative link exceeds limit.
	 * 
	 * @param transition_event_id
	 * @throws IdNotExistException
	 */
	public void add(String transition_event_id) throws IdNotExistException {
		if (transition_event_manager
				.transitionEventIdExists(transition_event_id)) {
			if (transition_event_ids.size()>=limit){
				transition_event_ids.removeFirst();
			}
			transition_event_ids.addLast(transition_event_id);
		} else {
			throw new IdNotExistException(transition_event_id);
		}
	}

	public boolean containsId(String transition_event_id) {
		return transition_event_ids.contains(transition_event_id);
	}

	/**
	 * This method exposes the internal list, so read-only is recommended
	 * 
	 * @return
	 */
	public LinkedList<String> getTransitionEventIds() {
		return transition_event_ids;
	}

//	/**
//	 * Tests if two causative links have common driver. Two causative links
//	 * cannot have more than one common driver.
//	 * 
//	 * @param other
//	 * @return the ID of the common transition event. Null if there is no common
//	 *         driver.
//	 * @throws NullPointerException
//	 *             - if the input is null
//	 */
//
//	public String getCommonTransitionEvent(CausativeLink other)
//			throws NullPointerException {
//		if (other == null) {
//			throw new NullPointerException();
//		} else {
//			transition_event_manager.getTransitionEvent(other);
//			for (String id : transition_event_ids) {
//				if (other.containsId(id)) {
//					return id;
//				}
//			}
//			return null;
//		}
//	}
	
	/**
	 * Return the common ambiguous delay that have to be subtracted from the events' scheduled
	 * times in order to get an accurate order of which happened first.  The two CausativeLinks
	 * must belong to two transition events of different type (start and end)
	 * @param link1 belonging to a start event
	 * @param link2 belonging to an end event
	 * @return
	 * @throws IdNotExistException 
	 */
	public static long getCommonAmbiguousDelay(CausativeLink link1, CausativeLink link2) throws IdNotExistException{
		if (link1 == null || link2 == null){
			throw new NullPointerException();
		}else{
			ArrayList<String> link1_gate_ids = getListOfGateIDs(link1);
			ArrayList<String> link2_gate_ids = getListOfGateIDs(link2);
			int i = 0;
			boolean found = false;
			long gate_delay = -1;
			while (i<link1_gate_ids.size()){
				int j = 0;
				while (j<link2_gate_ids.size()){
					// Common gate found
					String gate_id = link1_gate_ids.get(i);
					if (link1_gate_ids.get(i).equals(link2_gate_ids.get(j))){
						gate_delay = Queries.getMaxDelay(gate_id)-Queries.getMinDelay(gate_id);
						// Check time difference
						long time1 = transition_event_manager.getTransitionEvent(link1.getTransitionEventIds().get(i)).getCircuitTime();
						long time2 = transition_event_manager.getTransitionEvent(link2.getTransitionEventIds().get(j)).getCircuitTime();
						if (time1 < time2 && (time2-time1) <= gate_delay){
							found = true;
							break;
						}
					}
					j++;
				}
				if (found){
					break;
				}
				i++;
			}
			return gate_delay;
		}
	}

	public CausativeLink copy() {
		return new CausativeLink(
				(LinkedList<String>) transition_event_ids.clone());
	}
	
	/**
	 * Get the list of Gate IDs associated with the CausativeLink
	 * @param cl
	 * @return
	 */
	private static ArrayList<String> getListOfGateIDs(CausativeLink cl){
		ArrayList<String> result = new ArrayList<String>();
		for (String event_id:cl.getTransitionEventIds()){
			TransitionEvent event = transition_event_manager.getTransitionEvent(event_id);
			result.add(event.getGateId());
		}
		return result;
	}
}
