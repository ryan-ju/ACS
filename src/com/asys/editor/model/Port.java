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
	
	public PortState exportState(){
		return new PortState();
	}
	
	private void importState(PortState state){
		this.parent = state.getParent();
		this.wire = state.getWire();
		this.rp.setPosition(state.getRoutingPoint().getX(), state.getRoutingPoint().getY());
	}

	abstract protected void setPosition(Point p);
	
	class PortState{
		private Wire wire;
		private Element parent;
		private RoutingPoint rp;
		
		PortState(){
			wire = Port.this.wire;
			parent = Port.this.parent;
			rp = new RoutingPoint(Port.this.rp);
		}
		
		public Wire getWire(){
			return wire;
		}
		
		public Element getParent(){
			return parent;
		}
		
		public RoutingPoint getRoutingPoint(){
			return rp;
		}
		
		public void restore(){
			Port.this.importState(this);
		}
	}
}
