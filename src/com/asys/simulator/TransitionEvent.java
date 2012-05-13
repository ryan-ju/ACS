/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.asys.constants.LogicValue;

/**
 * @author ryan
 * 
 */
public class TransitionEvent extends CircuitEvent {
	private final String gate_id;
	private final LogicValue new_value;
	private final ArrayList<String> causative_link;
	private final String transition_id;

	public TransitionEvent(String circuit_event_id, long circuit_time,
			Date real_time, String gate_id, LogicValue new_value,
			List<String> causative_link, String transition_id) {
		super(circuit_event_id, circuit_time, real_time);
		this.gate_id = gate_id;
		this.new_value = new_value;
		this.causative_link = new ArrayList<String>();
		causative_link.addAll(causative_link);
		this.transition_id = transition_id;
	}

	public String getGateId() {
		return gate_id;
	}

	public LogicValue getNewValue() {
		return new_value;
	}

	public ArrayList<String> getCausativeLink() {
		return causative_link;
	}
	
	public String getTransitionId(){
		return transition_id;
	}

}
