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
//		parent.setPositionOfInport(this);
	}

	/**
	 * Binding the port to the wire, as well as the wire to the port.
	 */
	@Override
	protected void setWire(Wire wire){
		super.setWire(wire);
		wire.setInport(this);
	}
	
	protected void setPosition(Point p) {
		this.rp.setPosition(p.getX(), p.getY());
//		int index = getParent().getIndexOfInport(this);
//		assert index > 0;
//		Direction ort = getParent().getOrientation();
//		if (ort == Direction.UP){
//			this.x = getParent().getX()+1+index;
//			this.y = getParent().getY()+getParent().getHeight();
//		}else if(ort == Direction.DOWN){
//			this.x = getParent().getX()+1+index;
//			this.y = getParent().getY();
//		}else if(ort == Direction.RIGHT){
//			this.x = getParent().getX();
//			this.y = getParent().getY()+1+index;
//		}else{
//			assert ort == Direction.LEFT;
//			this.x = getParent().getX()+getParent().getWidth();
//			this.y = getParent().getY()+1+index;
//		}
//		super.updateRoutingPointPosition();
	}
	
	public LogicValue getValue() throws NoWireException{
		if (this.getWire()==null) throw new NoWireException();
		return this.getWire().getOutport().getValue();
	}
}
