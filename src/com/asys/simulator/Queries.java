/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.HashMap;

import com.asys.constants.ElementPropertyKey;
import com.asys.constants.LogicValue;
import com.asys.editor.model.Element;
import com.asys.editor.model.EnvironmentGate;
import com.asys.editor.model.Property;
import com.asys.editor.model.Protocol;
import com.asys.model.components.exceptions.InvalidPropertyKeyException;
import com.asys.simulator.exceptions.IdNotExistException;

/**
 * @author ryan
 *
 */
public class Queries {
	static GateFactory gm = GateFactory.getInstance();
	static PortFactory pm = PortFactory.getInstance();
	
	/**
	 * Return a list of InputPort IDs that are different from ip_id but are on the same gate.
	 * @param ip_id
	 * @return
	 * @throws IdNotExistException 
	 */
	public static ArrayList<String> getOtherInputPorts(String ip_id) throws IdNotExistException{
		ArrayList<String> others = new ArrayList<String>();
		InputPort ip = pm.getInputPort(ip_id);
		Gate gate = gm.getGate(ip.getGateId());
		for (String ipp_id:gate.getInputPortIds()){
			if (!ipp_id.equals(ip_id)){
				others.add(ipp_id);
			}
		}
		return others;
	}
	
	public static LogicValue getValueOnPort(String port_id) throws IdNotExistException{
		if (pm.isInputPort(port_id)){
			InputPort ip = pm.getInputPort(port_id);
			OutputPort op = pm.getOutputPort(ip.getOutputPortId());
			Gate gate = gm.getGate(op.getGateId());
			return gate.getCurrentLogicValue();
		}else{
			OutputPort op = pm.getOutputPort(port_id);
			Gate gate = gm.getGate(op.getGateId());
			return gate.getCurrentLogicValue();
		}
	}
	
	/**
	 * @param local_input_cache - a map of input_port_id to local logic value
	 * @param ip_id
	 * @return
	 * @throws IdNotExistException 
	 */
	public static ArrayList<LogicValue> getOtherInputValues(HashMap<String, LogicValue> local_input_cache, String ip_id) throws IdNotExistException{
		ArrayList<LogicValue> result = new ArrayList<LogicValue>();
		ArrayList<String> others = getOtherInputPorts(ip_id);
		for (String other:others){
			if (local_input_cache.containsKey(other)){
				result.add(local_input_cache.get(other));
			}else{
				result.add(getValueOnPort(other));
			}
		}
		return result;
	}
	
	public static String getParentGateId(String output_port_id) throws IdNotExistException{
		return pm.getOutputPort(output_port_id).getGateId();
	}
	
	/**
	 * This method cannot be used on environment gates.
	 * @param gate_id
	 * @return
	 * @throws InvalidPropertyKeyException 
	 * @throws IdNotExistException 
	 */
	public static long getMinDelay(String gate_id) throws IdNotExistException{
		Gate gate = gm.getGate(gate_id);
		Element elt = gate.getElement();
		assert !(elt instanceof EnvironmentGate);
		Property property = elt.getProperty();
		try {
			return (Long) property.getProperty(ElementPropertyKey.MIN_DELAY);
		} catch (InvalidPropertyKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * This method cannot be used on environment gates.
	 * @param gate_id
	 * @return
	 * @throws InvalidPropertyKeyException 
	 * @throws IdNotExistException 
	 */
	public static long getMaxDelay(String gate_id) throws IdNotExistException{
		Gate gate = gm.getGate(gate_id);
		Element elt = gate.getElement();
		assert !(elt instanceof EnvironmentGate);
		Property property = elt.getProperty();
		try {
			return (Long) property.getProperty(ElementPropertyKey.MAX_DELAY);
		} catch (InvalidPropertyKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * This method can only be used on environment gates.
	 * @param gate_id
	 * @return
	 * @throws IdNotExistException 
	 */
	public static AbstractScript getScript(String gate_id) throws IdNotExistException{
		Gate gate = gm.getGate(gate_id);
		Element elt = gate.getElement();
		assert elt instanceof EnvironmentGate;
		Property property = elt.getProperty();
		Protocol protocol = null;
		try {
			protocol = (Protocol) property.getProperty(ElementPropertyKey.PROTOCOL);
		} catch (InvalidPropertyKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert protocol != null;
		AbstractScript script = protocol.getScript();
		return script;
	}
}
