/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;

import com.asys.constants.LogicValue;
import com.asys.editor.model.Element;
import com.asys.simulator.exceptions.IdExistException;
import com.asys.simulator.exceptions.IdNotExistException;

/**
 * @author ryan
 *
 */
public class Gate {
	private final String gate_id;
	private final String gate_name;
	private final Element element;
	private LogicValue current_logic_value;
	private final ArrayList<String> scheduled_events;
	private final ArrayList<String> input_port_ids;
	private String output_port_id;
	
	public Gate(String gate_id, String gate_name, Element element, LogicValue current_logic_value, ArrayList<String> input_port_ids, String output_port_id){
		this.gate_id=gate_id;
		this.gate_name=gate_name;
		this.element = element;
		this.current_logic_value=current_logic_value;
		this.scheduled_events = new ArrayList<String>();
		this.input_port_ids = new ArrayList<String>();
		if (input_port_ids != null){
			this.input_port_ids.addAll(input_port_ids);
		}
		this.output_port_id = output_port_id;
	}
	
	public String getGateId(){
		return gate_id;
	}
	
	public String getGateName(){
		return gate_name;
	}
	
	public Element getElement(){
		return element;
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
	
	public ArrayList<String> getInputPortIds(){
		return input_port_ids;
	}
	
	public void addInputPortId(String input_port_id) throws IdNotExistException{
		if (PortFactory.getInstance().containsInputPortId(input_port_id)){
			input_port_ids.add(input_port_id);
		}else{
			throw new IdNotExistException(input_port_id);
		}
	}
	
	public String getOutputPortId(){
		return output_port_id;
	}
	
	public void setOutputPordId(String output_port_id){
		this.output_port_id = output_port_id;
	}
	
}
