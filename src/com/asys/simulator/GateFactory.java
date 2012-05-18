/**
 * 
 */
package com.asys.simulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.asys.constants.LogicValue;
import com.asys.editor.model.AndGate;
import com.asys.editor.model.CGate;
import com.asys.editor.model.Element;
import com.asys.editor.model.EnvironmentGate;
import com.asys.editor.model.Fanout;
import com.asys.editor.model.InputGate;
import com.asys.editor.model.NandGate;
import com.asys.editor.model.NorGate;
import com.asys.editor.model.NotGate;
import com.asys.editor.model.OrGate;
import com.asys.editor.model.OutputGate;
import com.asys.editor.model.XorGate;
import com.asys.simulator.exceptions.IdNotExistException;
import com.asys.simulator.exceptions.InvalidElementException;

/**
 * @author ryan
 * 
 */
public class GateFactory implements GateManager {
	private final HashMap<String, Gate> map;
	private final HashMap<Element, String> inner_map;
	private static GateFactory instance;
	private int next_available_index;
	private int next_input_gate_index, next_output_gate_index,
			next_env_gate_index, next_not_gate_index, next_and_gate_index,
			next_or_gate_index, next_nand_gate_index, next_nor_gate_index,
			next_xor_gate_index, next_c_gate_index, next_fanout_index;

	public static GateFactory getInstance() {
		if (instance == null) {
			instance = new GateFactory();
		}
		return instance;
	}

	private GateFactory() {
		this.map = new HashMap<String, Gate>();
		this.inner_map = new HashMap<Element, String>();
		next_available_index = 0;
		next_input_gate_index = 0;
		next_output_gate_index = 0;
		next_env_gate_index = 0;
		next_not_gate_index = 0;
		next_and_gate_index = 0;
		next_or_gate_index = 0;
		next_nand_gate_index = 0;
		next_nor_gate_index = 0;
		next_xor_gate_index = 0;
		next_c_gate_index = 0;
		next_fanout_index = 0;
	}

	public String createGate(Element element) throws InvalidElementException {
		String gate_id = getNextAvailabeId();
		if (element instanceof InputGate) {
			map.put(gate_id, new Gate(gate_id, "input"
					+ (next_input_gate_index++), element, LogicValue.X, null,
					null));
		} else if (element instanceof OutputGate) {
			map.put(gate_id, new Gate(gate_id, "output"
					+ (next_output_gate_index++), element, LogicValue.X, null,
					null));
		} else if (element instanceof EnvironmentGate) {
			map.put(gate_id, new Gate(gate_id, "environment"
					+ (next_env_gate_index++), element, LogicValue.X, null,
					null));
		} else if (element instanceof NotGate) {
			map.put(gate_id, new Gate(gate_id, "not"
					+ (next_not_gate_index++), element, LogicValue.X, null,
					null));
		} else if (element instanceof AndGate) {
			map.put(gate_id,  new Gate(gate_id, "and"
					+ (next_and_gate_index++), element, LogicValue.X, null,
					null));
		} else if (element instanceof OrGate) {
			map.put(gate_id,  new Gate(gate_id, "or" + (next_or_gate_index++),
					element, LogicValue.X, null, null));
		} else if (element instanceof NandGate) {
			map.put(gate_id,  new Gate(gate_id, "nand"
					+ (next_nand_gate_index++), element, LogicValue.X, null,
					null));
		} else if (element instanceof NorGate) {
			map.put(gate_id,  new Gate(gate_id, "nor"
					+ (next_nor_gate_index++), element, LogicValue.X, null,
					null));
		} else if (element instanceof XorGate) {
			map.put(gate_id,  new Gate(gate_id, "xor"
					+ (next_xor_gate_index++), element, LogicValue.X, null,
					null));
		} else if (element instanceof CGate) {
			map.put(gate_id,  new Gate(gate_id, "c" + (next_c_gate_index++),
					element, LogicValue.X, null, null));
		} else if (element instanceof Fanout) {
			map.put(gate_id,  new Gate(gate_id, "fanout"
					+ (next_fanout_index++), element, LogicValue.X, null, null));
		} else {
			throw new InvalidElementException(element.getClass().toString());
		}
		inner_map.put(element, gate_id);
		System.out.println("GateFactory.createGate(), inner_map has put <"+element.toString()+", "+gate_id+">");
		return gate_id;
	}

	public void initialize(InitializationSheet sheet) {
		Map<String, LogicValue> sheet_map = sheet.getSheetMap();
		for (Entry<String, LogicValue> entry : sheet_map.entrySet()) {
			map.get(entry.getKey()).setCurrentLogicValue(entry.getValue());
		}
	}

	@Override
	public Gate getGate(String id) throws IdNotExistException {
		if (map.containsKey(id)) {
			return map.get(id);
		} else {
			throw new IdNotExistException(id);
		}
	}

	@Override
	public Set<String> getGateIds() {
		return map.keySet();
	}

	@Override
	public boolean gateIdExists(String id) {
		return map.containsKey(id);
	}
	
	@Override
	public void clear(){
		this.map.clear();
		this.inner_map.clear();
		next_available_index = 0;
		next_input_gate_index = 0;
		next_output_gate_index = 0;
		next_env_gate_index = 0;
		next_not_gate_index = 0;
		next_and_gate_index = 0;
		next_or_gate_index = 0;
		next_nand_gate_index = 0;
		next_nor_gate_index = 0;
		next_xor_gate_index = 0;
		next_c_gate_index = 0;
		next_fanout_index = 0;
	}
	
	public String getIdByElement(Element elt){
		return inner_map.get(elt);
	}
	
	public String getGateNameByElement(Element elt){
		String gate_id = inner_map.get(elt);
		if (gate_id == null) return null;
		Gate gate = map.get(gate_id);
		if (gate == null) return null;
		return gate.getGateName();
	}

	private String getNextAvailabeId() {
		return "g" + (next_available_index++);
	}
	
	/**
	 * Returns a log of the state of this instance.  The log has the format:
	 * [gate_id]|[input_port_id], ...|[output_port_id]
	 * ...
	 * 
	 * @return
	 */
	protected String dump(){
		StringBuffer buf = new StringBuffer();
		for (Entry<String, Gate> entry:map.entrySet()){
			buf.append(entry.getKey()).append("|").append(entry.getValue().getGateName()).append("|");
			Iterator<String> it = entry.getValue().getInputPortIds().iterator();
			while (it.hasNext()){
				buf.append(it.next());
				if (it.hasNext()){
					buf.append(", ");
				}
			}
			buf.append("|");
			buf.append(entry.getValue().getOutputPortId());
			buf.append("\n");
		}
		return buf.toString();
	}

}
