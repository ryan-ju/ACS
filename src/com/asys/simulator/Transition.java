/**
 * 
 */
package com.asys.simulator;

/**
 * @author ryan
 * 
 */
public class Transition {
	private final String transition_id;
	private final String start_event_id;
	private final String end_event_id;

	public Transition(String transition_id, String start_event_id,
			String end_event_id) {
		this.transition_id = transition_id;
		this.start_event_id = start_event_id;
		this.end_event_id = end_event_id;
	}
	
	public String getTransitionId(){
		return transition_id;
	}
	
	public String getStartEventId(){
		return start_event_id;
	}
	
	public String getEndEventId(){
		return end_event_id;
	}
}
