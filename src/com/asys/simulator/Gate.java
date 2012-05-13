/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.List;

import com.asys.constants.LogicValue;

/**
 * @author ryan
 *
 */
public class Gate {
	private final String gate_id;
	private final String gate_name;
	private LogicValue current_logic_value;
	private final ArrayList<String> scheduled_events;
	private final ArrayList<String> children;
	
	public Gate(String gate_id, String gate_name, LogicValue current_logic_value, ArrayList<String> children){
		this.gate_id=gate_id;
		this.gate_name=gate_name;
		this.current_logic_value=current_logic_value;
		this.scheduled_events = new ArrayList<String>();
		this.children = new ArrayList<String>();
		this.children.addAll(children);
	}
	
	public Gate(String gate_id, String gate_name, LogicValue current_logic_value){
		this.gate_id=gate_id;
		this.gate_name=gate_name;
		this.current_logic_value=current_logic_value;
		this.scheduled_events = new ArrayList<String>();
		this.children = new ArrayList<String>();
	}
	
	public String getGateId(){
		return gate_id;
	}
	
	public String getGateName(){
		return gate_name;
	}
	
	public LogicValue getCurrentLogicValue(){
		return current_logic_value;
	}
	
	public void setCurrentLogicValue(LogicValue value){
		this.current_logic_value = value;
	}
	
	public ArrayList<String> getScheduledEvents(){
		return scheduled_events;
	}
	
	public ArrayList<String> getChildren(){
		return children;
	}
	
	public void setChildren(List<String> children){
		this.children.clear();
		this.children.addAll(children);
	}
}
