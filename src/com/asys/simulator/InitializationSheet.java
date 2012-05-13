/**
 * 
 */
package com.asys.simulator;

import java.util.HashMap;

import com.asys.constants.LogicValue;

/**
 * @author ryan
 *
 */
public class InitializationSheet {
	private HashMap<String, LogicValue> sheet;
	
	public InitializationSheet(){
		this.sheet = new HashMap<String, LogicValue>();
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String, LogicValue> getSheetMap(){
		return (HashMap<String, LogicValue>) sheet.clone();
	}
	
	@SuppressWarnings("unchecked")
	public void setSheet(HashMap<String, LogicValue> sheet){
		this.sheet = (HashMap<String, LogicValue>) sheet.clone();
	}
	
	public LogicValue setValue(String gate_id, LogicValue value){
		return sheet.put(gate_id, value);
	}
	
}
