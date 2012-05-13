/**
 * 
 */
package com.asys.constants;

import java.util.List;

/**
 *
 */
public enum LogicValue {
	ZERO, ONE, X;
	
	@Override
	public String toString(){
		if (this.equals(ZERO)) return "0";
		else if (this.equals(ONE)) return "1";
		else if (this.equals(X)) return "X";
		else return "";
	}
	
	public static LogicValue not(LogicValue l1) {
		return null;
	}
	public static LogicValue not(List<LogicValue> ls) {
		return null;
	}
	
	public static LogicValue and(LogicValue l1, LogicValue l2) {
		return null;
	}
	public static LogicValue and(List<LogicValue> ls) {
		return null;
	}
	public static LogicValue or(LogicValue l1, LogicValue l2) {
		return null;
	}
	public static LogicValue or(List<LogicValue> ls) {
		return null;
	}
	public static LogicValue nand(LogicValue l1, LogicValue l2) {
		return null;
	}
	public static LogicValue nand(List<LogicValue> ls) {
		return null;
	}
	public static LogicValue nor(LogicValue l1, LogicValue l2) {
		return null;
	}
	public static LogicValue nor(List<LogicValue> ls) {
		return null;
	}
	public static LogicValue xor(LogicValue l1, LogicValue l2) {
		return null;
	}
	public static LogicValue xor(List<LogicValue> ls) {
		return null;
	}
	public static LogicValue majority(LogicValue l1, LogicValue l2) {
		return null;
	}
	public static LogicValue majority(List<LogicValue> ls) {
		return null;
	}
}
