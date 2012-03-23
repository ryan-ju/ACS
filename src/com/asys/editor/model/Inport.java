/**
 * 
 */
package com.asys.editor.model;

import com.asys.constants.LogicValue;
import com.asys.model.components.exceptions.NoWireException;

/**
 * @author ryan
 * 
 */
public class Inport extends Port {
	public Inport(Element parent) {
		super(parent);
		// parent.setPositionOfInport(this);
	}

	/**
	 * Binding the port to the wire, as well as the wire to the port.
	 */
	@Override
	protected void setWire(Wire wire) {
		super.setWire(wire);
		if (wire != null) {
			wire.setInport(this);
		}
	}

	protected void setPosition(Point p) {
		this.rp.setPosition(p.getX(), p.getY());
	}

	public LogicValue getValue() throws NoWireException {
		if (this.getWire() == null)
			throw new NoWireException();
		return this.getWire().getOutport().getValue();
	}
}
