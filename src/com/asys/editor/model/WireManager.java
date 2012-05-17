/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ryan
 * 
 */
public class WireManager {
	private static CircuitManager cm;
	private ArrayList<Wire> wires;
	private static EdgeManager edgm;

	public WireManager() {
		this.wires = new ArrayList<Wire>();

	}

	public void init() {
		cm = CircuitManager.getInstance();
		edgm = cm.getEdgeManager();
	}
	
	public void clear(){
		this.wires = new ArrayList<Wire>();
	}

	protected void addWire(Wire new_wire) {
		wires.add(new_wire);
		update();
	}

	protected void addWire(List<Wire> new_wires) {
		wires.addAll(new_wires);
		update();
	}

	protected boolean remove(Wire rem_wire) {
		boolean b = wires.remove(rem_wire);
		if (b)
			update();
		return b;
	}

	protected boolean remove(List<Wire> rem_wires) {
		boolean b = wires.removeAll(rem_wires);
		if (b)
			update();
		return b;
	}

	public ArrayList<Wire> getWires() {
		return (ArrayList<Wire>) wires.clone();
	}

	/**
	 * This method is only called by selection manager to decide
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Wire getWireAt(int x, int y) {
		WireEdge edge = edgm.getEdgeAt(x, y);
		if (edge == null) {
			return null;
		} else {
			return edge.getParent();
		}
	}

	public Rectangle getWiresBound() {
		int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE, x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE;
		for (Wire wire : wires) {
			for (RoutingPoint rp : wire.getRoutingPoints()) {
				int x = rp.getX();
				int y = rp.getY();
				if (x < x1)
					x1 = x;
				if (y < y1)
					y1 = y;
				if (x > x2)
					x2 = x;
				if (y > y2)
					y2 = y;
			}
		}
		return new Rectangle(x1, y1, x2 - x1, y2 - y1);
	}

	protected void update() {
		// cm.setWireManagerChanged();
		edgm.update();
	}
}
