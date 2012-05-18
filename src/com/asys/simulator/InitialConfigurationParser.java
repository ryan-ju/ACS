/**
 * 
 */
package com.asys.simulator;

import java.util.HashMap;
import java.util.Scanner;
import java.util.Map.Entry;

import com.asys.constants.LogicValue;

/**
 * @author ryan
 *
 */
public class InitialConfigurationParser {
	
	public static HashMap<String, LogicValue> parse(String str){
		HashMap<String, LogicValue> map = new HashMap<String, LogicValue>();
		for (String gate_id:GateFactory.getInstance().getGateIds()){
			map.put(gate_id, LogicValue.X);
		}
		
		Scanner scanln = new Scanner(str);
		while (scanln.hasNext()){
			Scanner scan = new Scanner(scanln.nextLine());
			String gate_id = scan.next();
			LogicValue lv = LogicValue.parse(scan.next());
			if (lv != null){
				map.put(gate_id, lv);
			}
		}
		return map;
	}
	
	public static String visualize(HashMap<String, LogicValue> map){
		StringBuffer buf = new StringBuffer();
		for (Entry<String, LogicValue> entry:map.entrySet()){
			buf.append(entry.getKey()).append(", ").append(entry.getValue()).append("\n");
		}
		return buf.toString();
	}
	
}
