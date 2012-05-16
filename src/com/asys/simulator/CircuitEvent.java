/**
 * 
 */
package com.asys.simulator;


/**
 * @author ryan
 *
 */
public class CircuitEvent {
	private String circuit_event_id;
	private long circuit_time;
	private long real_time;
	
	public CircuitEvent(String circuit_event_id, long circuit_time, long real_time){
		this.circuit_event_id = circuit_event_id;
		this.circuit_time = circuit_time;
		this.real_time = real_time;
	}
	
	public String getId(){
		return circuit_event_id;
	}
	
	public long getCircuitTime(){
		return circuit_time;
	}
	
	public long getRealTime(){
		return real_time;
	}
}
