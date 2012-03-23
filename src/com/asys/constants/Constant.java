/**
 * 
 */
package com.asys.constants;

import java.awt.Color;

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
	public static final int GRID_SIZE = 12;
	public static final float HIGHLIGHT_RADIUS_RATIO = 0.4f,
			HIGHLIGHT_TRANSPARANCY = 0.6f, GATE_MARGIN_RATIO = 0.08f;
	public static float HIGHLIGT_RADIUS = GRID_SIZE * HIGHLIGHT_RADIUS_RATIO;
	public static final int MAX_ACTION_QUEUE_LENGTH = 50;
	public static final int NULL = 0, ELEMENT = 1, WIRE = 2, WIRE_EDGE = 3;
	public static final Color BACKGROUND_CLR = new Color(255, 255, 220),
			GRID_CLR = Color.DARK_GRAY, WIRE_CLR = Color.BLACK,
			ELEMENT_BORDER_CLR = Color.BLACK,
			ELEMENT_BACKGROUND_COLOR = Color.WHITE,
			ELEMENT_HIGHLIGHT_CLR = Color.BLUE,
			WIRE_HIGHLIGHT_CLR = Color.BLUE,
			WIRE_EDGE_HIGHLIGHT_CLR = Color.MAGENTA, ERROR_CLR = Color.RED,
			WIRE_CREATION_CLR = Color.DARK_GRAY,
			WIRE_EDGE_CREATION_CLR = Color.ORANGE,
			SELECTION_RECTANGLE_CLR = new Color(0, 0, 150),
			GATE_BUTTON_BACKGROUND_CLR = Color.WHITE,
			NOTIFIER_NORMAL_BACKGROUND_CLR = Color.WHITE,
			NOTIFIER_ALERT_BACKGROUND_CLR = new Color(255, 150, 150),
			PROPERTY_VIEWER_LABEL_NORMAL_BG_CLR = Color.WHITE,
			PROPERTY_VIEWER_LABEL_FOCUS_BG_CLR = Color.ORANGE,
			PROPERTY_VIEWER_TEXT_EDITABLE_NORMAL_BG_CLR = Color.WHITE,
			PROPERTY_VIEWER_TEXT_INEDITABLE_NORMAL_BG_CLR = new Color(220, 220, 220),
			PROPERTY_VIEWER_TEXT_FOCUS_BG_CLR = Color.ORANGE;
	public static final float WIRE_CREATION_WIDTH_RATIO = 0.5f,
			WIRE_EDGE_CREATION_WIDTH_RATIO = 0.8f;
	public static final int WIRE_CREATION_WIDTH = (int) Math
			.round(0.5 * GRID_SIZE), WIRE_EDGE_CREATION_WIDTH = (int) Math
			.round(0.8 * GRID_SIZE);
	public static final int DEFAULT_CANVAS_WIDTH = 1000,
			DEFAULT_CANVAS_HEIGHT = 1000;
	public static final String IMAGE_PATH = "bin/images/";
	public static final int GATE_MARGIN = (int) GRID_SIZE / 2,
			GATE_KNOB_SIZE = (int) GRID_SIZE / 4,
			FANOUT_RADIUS = GRID_SIZE / 3;
	public static final int NOTIFICATION_DELAY = 5000;
	public static final char UNDO = 'z', REDO = 'y', ROTATE_LEFT = 'l',
			ROTATE_RIGHT = 'r';

	public static Property getDefaultProperty() {
		Property p = new Property();
		try {
			p.addKey(ElementPropertyKey.NUM_INPORT);
			p.addKey(ElementPropertyKey.NUM_OUTPORT);
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
