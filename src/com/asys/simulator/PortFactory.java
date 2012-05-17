/**
 * 
 */
package com.asys.simulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.asys.editor.model.Inport;
import com.asys.editor.model.Outport;
import com.asys.simulator.exceptions.IdNotExistException;

/**
 * @author ryan
 * 
 */
public class PortFactory implements PortManager {
	private HashMap<String, InputPort> input_port_map;
	private HashMap<String, OutputPort> output_port_map;
	private int input_port_index, output_port_index;
	private static PortFactory instance;

	public static PortFactory getInstance() {
		if (instance == null) {
			instance = new PortFactory();
		}
		return instance;
	}

	private PortFactory() {
		this.input_port_map = new HashMap<String, InputPort>();
		this.output_port_map = new HashMap<String, OutputPort>();
		input_port_index = 0;
		output_port_index = 0;
	}

	/**
	 * InputPort has ID of the form "ip:([gate_id]):[index]"
	 * 
	 * @param gate_id
	 * @return
	 */
	public String createInputPort(String gate_id, Inport ip) {
		int index = getNextInputPortIndex();
		String input_port_id = "ip:" + "(" + gate_id + "):" + index;
		InputPort input_port = new InputPort(input_port_id, null, gate_id, ip);
		input_port_map.put(input_port_id, input_port);
		return input_port_id;
	}

	/**
	 * InputPort has ID of the form "op:([gate_id]):[index]"
	 * 
	 * @param gate_id
	 * @return
	 */
	public String createOutputPort(String gate_id, Outport op) {
		int index = getNextOutputPortIndex();
		String output_port_id = "op:" + "(" + gate_id + "):" + index;
		OutputPort output_port = new OutputPort(output_port_id, null, gate_id,
				op);
		output_port_map.put(output_port_id, output_port);
		return output_port_id;
	}

	private int getNextInputPortIndex() {
		return input_port_index++;
	}

	private int getNextOutputPortIndex() {
		return output_port_index++;
	}

	@Override
	public InputPort getInputPort(String input_port_id)
			throws IdNotExistException {
		if (input_port_map.containsKey(input_port_id)) {
			return input_port_map.get(input_port_id);
		} else {
			throw new IdNotExistException(input_port_id);
		}
	}

	@Override
	public OutputPort getOutputPort(String output_port_id)
			throws IdNotExistException {
		if (output_port_map.containsKey(output_port_id)) {
			return output_port_map.get(output_port_id);
		} else {
			throw new IdNotExistException(output_port_id);
		}
	}

	@Override
	public boolean containsInputPortId(String input_port_id) {
		return input_port_map.containsKey(input_port_id);
	}

	@Override
	public boolean containsOutputPortId(String output_port_id) {
		return output_port_map.containsKey(output_port_id);
	}

	@Override
	public Set<String> getInputPortIds() {
		return input_port_map.keySet();
	}

	@Override
	public Set<String> getOutputPortIds() {
		return output_port_map.keySet();
	}
	
	@Override
	public boolean isInputPort(String port_id) throws IdNotExistException{
		if (port_id.startsWith("ip") && containsInputPortId(port_id)){
			return true;
		}else if (port_id.startsWith("op") && containsOutputPortId(port_id)){
			return false;
		}else{
			throw new IdNotExistException(port_id);
		}
	}
	
	@Override
	public void clear(){
		this.input_port_map = new HashMap<String, InputPort>();
		this.output_port_map = new HashMap<String, OutputPort>();
		input_port_index = 0;
		output_port_index = 0;
	}

	/**
	 * Returns a log of the state of this instance.  The log has the format:
	 * [input_port_id]|[gate_id]|[output_port_id of the output port associated with the input port with input_port_id]
	 * ...
	 * [output_port_id]|[gate_id]|[input_port_id associated], ...
	 * ...
	 * @return
	 */
	public String dump() {
		StringBuffer buf = new StringBuffer();
		for (Entry<String, InputPort> entry : input_port_map.entrySet()) {
			buf.append(entry.getKey()).append("|")
					.append(entry.getValue().getGateId()).append("|")
					.append(entry.getValue().getOutputPortId()).append("\n");
		}
		for (Entry<String, OutputPort> entry: output_port_map.entrySet()){
			buf.append(entry.getKey()).append("|").append(entry.getValue().getGateId()).append("|");
			Iterator<String> it = entry.getValue().getInputPortIds().iterator();
			while (it.hasNext()){
				buf.append(it.next());
				if (it.hasNext()){
					buf.append(", ");
				}
			}
			buf.append("\n");
		}
		return buf.toString();
	}
}
