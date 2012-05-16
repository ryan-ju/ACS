/**
 * 
 */
package com.asys.simulator;

import com.asys.editor.model.Inport;

/**
 * @author ryan
 *
 */
public class InputPort {
	private final String input_port_id;
	private String output_port_id;
	private final String gate_id;
	private final Inport ip;
	
	public InputPort(String input_port_id, String output_port_id, String gate_id, Inport ip){
		this.input_port_id = input_port_id;
		this.output_port_id = output_port_id;
		this.gate_id = gate_id;
		this.ip = ip;
	}
	
	public String getInputPortId(){
		return input_port_id;
	}
	
	public String getOutputPortId(){
		return output_port_id;
	}
	
	public void setOutputPortId(String output_port_id){
		this.output_port_id = output_port_id;
	}
	
	public String getGateId(){
		return gate_id;
	}
	
	public Inport getInport(){
		return ip;
	}
}
