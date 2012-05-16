/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.List;

import com.asys.editor.model.Outport;
import com.asys.simulator.exceptions.IdExistException;
import com.asys.simulator.exceptions.IdNotExistException;

/**
 * @author ryan
 * 
 */
public class OutputPort {
	private final String output_port_id;
	private final ArrayList<String> input_port_ids;
	private final String gate_id;
	private final Outport op;

	public OutputPort(String output_port_id, List<String> input_port_ids,
			String gate_id, Outport op) {
		this.output_port_id = output_port_id;
		this.input_port_ids = new ArrayList<String>();
		if (input_port_ids != null) {
			this.input_port_ids.addAll(input_port_ids);
		}
		this.gate_id = gate_id;
		this.op = op;
	}

	public String getOutputPortId() {
		return output_port_id;
	}

	public ArrayList<String> getInputPortIds() {
		return input_port_ids;
	}

	public void setInputPortIds(List<String> input_port_ids) {
		this.input_port_ids.clear();
		this.input_port_ids.addAll(input_port_ids);
	}
	
	public void addInputPortId(String input_port_id) throws IdExistException, IdNotExistException{
		if (input_port_ids.contains(input_port_id)){
			throw new IdExistException(input_port_id);
		}else{
			if (PortFactory.getInstance().containsInputPortId(input_port_id)){
			input_port_ids.add(input_port_id);
			}else{
				throw new IdNotExistException(input_port_id);
			}
		}
	}

	public String getGateId() {
		return gate_id;
	}
	
	public Outport getOutport(){
		return op;
	}
}
