/**
 * 
 */
package com.asys.constants;

import com.asys.editor.model.Protocol;

/**
 *
 */
public enum ElementPropertyKey {
	NUM_INPORT, NUM_OUTPORT, MIN_DELAY, MAX_DELAY, PROTOCOL;

	public static boolean isValueValid(ElementPropertyKey key, Object value) {
		if (key == NUM_INPORT && value instanceof Integer) {
			return true;
		} else if (key == NUM_OUTPORT && value instanceof Integer) {
			return true;
		} else if (key == MIN_DELAY && value instanceof Long) {
			return true;
		} else if (key == MAX_DELAY && value instanceof Long) {
			return true;
		} else if (key == PROTOCOL && value instanceof Protocol) {
			return true;
		}
		return false;
	}
}
