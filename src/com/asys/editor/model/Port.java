/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 * 
 */
public abstract class Port {
	private Wire wire;
	private Element parent;
	protected RoutingPoint rp;

	public Port(Element parent) {
		this.parent = parent;
		this.rp = new RoutingPoint(0, 0);
	}

	public Wire getWire() {
		return wire;
	}

	protected void setWire(Wire wire) {
		this.wire = wire;
	}

	public Element getParent() {
		return parent;
	}

	public RoutingPoint getPosition() {
		return new RoutingPoint(rp);
	}

	abstract protected void setPosition(Point p);

}
