/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.asys.constants.LogicValue;
import com.asys.editor.model.AndGate;
import com.asys.editor.model.CGate;
import com.asys.editor.model.CircuitManager;
import com.asys.editor.model.CircuitUtilities;
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

/**
 * @author ryan
 * 
 */
public class GateFactory implements GateManager {
	private final HashMap<String, Gate> map;
	private static GateFactory instance;
	private int next_available_index;
	private int next_input_gate_index, next_output_gate_index,
			next_env_gate_index, next_not_gate_index, next_and_gate_index,
			next_or_gate_index, next_nand_gate_index, next_nor_gate_index,
			next_xor_gate_index, next_c_gate_index,
			next_fanout_index;

	public static GateFactory getInstance() {
		if (instance == null) {
			instance = new GateFactory();
		}
		return instance;
	}

	private GateFactory() {
		this.map = new HashMap<String, Gate>();
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

	public void build(CircuitManager circuit_manager){
		ArrayList<Element> elements = circuit_manager.getElementManager().getElements();
		HashMap<Element, String> temp_map = new HashMap<Element, String>();
		for (Element element:elements){
			String id = getNextAvailabeId();
			temp_map.put(element, id);
			map.put(id, createGate(element, getNextAvailabeId()));
		}
		for (Element element:elements){
			List<Object[]> neighbour_objs = CircuitUtilities.children(element);
			ArrayList<String> neighbour_ids = new ArrayList<String>();			
			for (Object[] neighbour_obj:neighbour_objs){
				Element neighbour = (Element) neighbour_obj[3];
				neighbour_ids.add(temp_map.get(neighbour));
			}
			Gate home_gate = map.get(temp_map.get(element));
			home_gate.setChildren(neighbour_ids);
		}
	}
	
	public void initialize(InitializationSheet sheet){
		Map<String, LogicValue> sheet_map = sheet.getSheetMap();
		for (Entry<String, LogicValue> entry:sheet_map.entrySet()){
			map.get(entry.getKey()).setCurrentLogicValue(entry.getValue());
		}
	}

	@Override
	public Gate getGate(String id) {
		return map.get(id);
	}

	@Override
	public boolean gateIdExists(String id) {
		return map.containsKey(id);
	}

	private String getNextAvailabeId() {
		return "g" + (next_available_index++);
	}

	private Gate createGate(Element element, String gate_id){
		if (element instanceof InputGate){
			return new Gate(gate_id, "input"+(next_input_gate_index++), LogicValue.X);
		}else if (element instanceof OutputGate){
			return new Gate(gate_id, "output"+(next_output_gate_index++), LogicValue.X);
		}else if (element instanceof EnvironmentGate){
			return new Gate(gate_id, "environment"+(next_env_gate_index++), LogicValue.X);
		}else if (element instanceof NotGate){
			return new Gate(gate_id, "not"+(next_not_gate_index++), LogicValue.X);
		}else if (element instanceof AndGate){
			return new Gate(gate_id, "and"+(next_and_gate_index++), LogicValue.X);
		}else if (element instanceof OrGate){
			return new Gate(gate_id, "or"+(next_or_gate_index++), LogicValue.X);
		}else if (element instanceof NandGate){
			return new Gate(gate_id, "nand"+(next_nand_gate_index++), LogicValue.X);
		}else if (element instanceof NorGate){
			return new Gate(gate_id, "nor"+(next_nor_gate_index++), LogicValue.X);
		}else if (element instanceof XorGate){
			return new Gate(gate_id, "xor"+(next_xor_gate_index++), LogicValue.X);
		}else if (element instanceof CGate){
			return new Gate(gate_id, "c"+(next_c_gate_index++), LogicValue.X);
		}else if (element instanceof Fanout){
			return new Gate(gate_id, "fanout"+(next_fanout_index++), LogicValue.X);
		}else{
			return null;
		}
		
	}
}
