/**
 * 
 */
package com.asys.editor.model;

import com.asys.constants.LogicValue;

/**
 * @author ryan
 * 
 */
public class Outport extends Port {
	private LogicValue value;

	public Outport(Element parent) {
		super(parent);
//		parent.setPositionOfOutport(this);
	}
	
	/**
	 * Binding the port to the wire, as well as the wire to the port.
	 */
	@Override
	protected void setWire(Wire wire){
		super.setWire(wire);
		wire.setOutport(this);
	}
	
	/**
	 * In the middle
	 */
	protected void setPosition(Point p) {
		this.rp.setPosition(p.getX(), p.getY());
//		Direction ort = getParent().getOrientation();
//		int h = getParent().getHeight();
//		assert h % 2 == 0; // h must be even.
//		h = h / 2;
//		if (ort == Direction.UP) {
//			this.y = getParent().getY();
//			this.x = getParent().getX() + h;
//		} else if (ort == Direction.DOWN) {
//			this.y = getParent().getY() + getParent().getHeight();
//			this.x = getParent().getX() + h;
//		} else if (ort == Direction.RIGHT) {
//			this.x = getParent().getX() + getParent().getWidth();
//			this.y = getParent().getY() + h;
//		} else {
//			assert ort == Direction.LEFT;
//			this.x = getParent().getX();
//			this.y = getParent().getY() + h;
//		}
//		super.updateRoutingPointPosition();
	}

	public LogicValue getValue() {
		return value;
	}

	protected void setValue(LogicValue value) {
		this.value = value;
	}
}
