/**
 * 
 */
package com.asys.constants;

import com.asys.editor.model.Property;
import com.asys.model.components.exceptions.InvalidPropertyException;
import com.asys.model.components.exceptions.InvalidPropertyKeyException;
import com.asys.model.components.exceptions.NoKeyException;

/**
 *
 */
public final class Constant {
	public static final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
	public static final int DEFAULT_GATE_WIDTH = 5, DEFAULT_GATE_HEIGHT = 4,
			MIN_GATE_WIDTH = 5, MIN_GATE_HEIGHT = 4;
	public static final Long DEFAULT_MIN_DELAY = (long) 500,
			DEFAULT_MAX_DELAY = (long) 1000;
	public static final int GRID_SIZE = 10;

	public static Property getDefaultProperty() {
		Property p = new Property();
		try {
			p.addKey(ElementPropertyKey.MIN_DELAY);
			p.addKey(ElementPropertyKey.MAX_DELAY);
			p.setProperty(ElementPropertyKey.MIN_DELAY, DEFAULT_MIN_DELAY);
			p.setProperty(ElementPropertyKey.MAX_DELAY, DEFAULT_MAX_DELAY);
		} catch (InvalidPropertyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p;

	}
}
