/**
 * 
 */
package com.asys.editor.model;

import java.util.LinkedList;

import com.asys.constants.Direction;

/**
 * @author ryan
 * 
 */
public class Wire {
	private Inport ip;
	private Outport op;
	private RoutingPoint inrp_old, outrp_old;
	private LinkedList<RoutingPoint> rps; // Excluding the start and end points.
	private LinkedList<WireEdge> res;
	private boolean hasChanged;
	
	private Wire(){
		// Do nothing.
	}
	
	public Wire(Outport op, Inport ip, LinkedList<RoutingPoint> rps) {
		this.ip = ip;
		inrp_old = new RoutingPoint(ip.getPosition());
		ip.setWire(this);
		this.op = op;
		outrp_old = new RoutingPoint(op.getPosition());
		op.setWire(this);
		this.rps = rps;
		unsetHasChanged();
	}

	public Inport getInport() {
		return ip;
	}

	/**
	 * Note this method is called by 'Inport.setWire'. Make sure when overriding
	 * this method, there is no infinite recursion.
	 * 
	 * @param ip
	 */
	protected void setInport(Inport ip) {
		this.ip = ip;
		setHasChanged();
	}

	public Outport getOutport() {
		return op;
	}

	/**
	 * Note this method is called by 'Outport.setWire'. Make sure when
	 * overriding this method, there is no infinite recursion.
	 * 
	 * @param ip
	 */
	protected void setOutport(Outport op) {
		this.op = op;
		setHasChanged();
	}

	/**
	 * The method copies one end point and adds it to the 'rps'.
	 * 
	 * Remember to call setHasChanged() after using this method.
	 * 
	 * @param isAtStart
	 *            true - the end point is at the Outport false - the end point
	 *            is at the Inport
	 */
	private void addRoutingPointAtEnd(boolean isAtStart) {
		if (isAtStart) {
			rps.addFirst(new RoutingPoint(getOutport().getPosition()));
		} else {
			rps.addLast(new RoutingPoint(getInport().getPosition()));
		}
	}
	
	/**
	 * 
	 * @param index
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return true - a new wire edge has been added
	 */
	protected boolean addRoutingPoints(int index, int x1, int y1, int x2, int y2) {
		assert index >= 0 && index < getRoutingEdges().size();
		WireEdge edge = getRoutingEdges().get(index);
		int min_ex = Math.min(edge.getP1().getX(), edge.getP2().getX());
		int max_ex = Math.max(edge.getP1().getX(), edge.getP2().getX());
		int min_ey = Math.min(edge.getP1().getY(), edge.getP2().getY());
		int max_ey = Math.max(edge.getP1().getY(), edge.getP2().getY());
		if (edge.isVertical()) {
			int min_y = Math.min(y1, y2);
			int max_y = Math.max(y1, y2);
			if (min_y <= min_ey || max_y >= max_ey || min_y == max_y) {
				return false;
			} else {
				assert edge.getP1().getY() != edge.getP2().getY();
				if (edge.getDirection() == Direction.DOWN) { // 'edge' points
																// downwards
					rps.add(index + 1, new RoutingPoint(min_ex, max_y));
					rps.add(index + 1, new RoutingPoint(min_ex, min_y));
				} else { // 'edge' points upwards
					assert edge.getDirection() == Direction.UP;
					rps.add(index + 1, new RoutingPoint(min_ex, min_y));
					rps.add(index + 1, new RoutingPoint(min_ex, max_y));
				}
				setHasChanged();
				return true;
			}
		} else {
			int min_x = Math.min(x1, x2);
			int max_x = Math.max(x1, x2);
			if (min_x <= min_ex || max_x >= max_ex || min_x == max_x) {
				return false;
			} else {
				assert edge.getP1().getX() != edge.getP2().getX();
				if (edge.getDirection() == Direction.RIGHT) { // 'edge' points
																// right
					rps.add(index + 1, new RoutingPoint(max_x, min_ey));
					rps.add(index + 1, new RoutingPoint(min_x, min_ey));
				} else { // 'edge' points left
					assert edge.getDirection() == Direction.LEFT;
					rps.add(index + 1, new RoutingPoint(min_x, min_ey));
					rps.add(index + 1, new RoutingPoint(max_x, min_ey));
				}
				setHasChanged();
				return true;
			}
		}
	}

	public LinkedList<RoutingPoint> getRoutingPoints() {
		return rps;
	}

	protected void setRoutingPoints(LinkedList<RoutingPoint> rps) {
		this.rps = rps;
		setHasChanged();
	}

	public LinkedList<WireEdge> getRoutingEdges() {
		if (hasChanged || res == null) {
			res = new LinkedList<WireEdge>();
			// RoutingPoint rpp = op.getPosition();
			RoutingPoint rpp = outrp_old;
			for (RoutingPoint rp : rps) {
				res.addLast(new WireEdge(rpp, rp, this));
				rpp = rp;
			}
			// res.add(new WireEdge(rpp, ip.getPosition()));
			res.add(new WireEdge(rpp, inrp_old, this));
			unsetHasChanged();
		}
		return res;
	}

	/**
	 * This method should only be called when moving both ends of the wire.
	 * 
	 * @param dx
	 * @param dy
	 */
	protected void move(int dx, int dy) {
		for (RoutingPoint rp : rps) {
			rp.setPosition(rp.getX() + dx, rp.getY() + dy);
		}
		inrp_old.move(dx, dy);
		outrp_old.move(dx, dy);
		fireWireChanged();
	}

	protected void moveEdge(int index, int dx, int dy) {
		assert index >= 0 && index < getRoutingEdges().size();
		WireEdge edge = getRoutingEdges().get(index);
		int size = getRoutingEdges().size();
		// If 'edge' is the first edge
		if (index == 0) {
			addRoutingPointAtEnd(true);
			setHasChanged();
			edge = getRoutingEdges().get(1);
		}
		// If 'edge' is the last edge
		if (index == size - 1) {
			addRoutingPointAtEnd(false);
			setHasChanged();
			edge = getRoutingEdges().get(getRoutingEdges().size() - 2); // Size
																		// could
																		// have
																		// potentially
																		// been
																		// changed
																		// in
																		// the
																		// first
																		// if
																		// block!
		}
		RoutingPoint rpa = edge.getP1();
		RoutingPoint rpb = edge.getP2();
		if (edge.isVertical()) {
			rpa.setX(rpa.getX() + dx);
			rpb.setX(rpb.getX() + dx);
		} else {
			rpa.setY(rpa.getY() + dy);
			rpb.setY(rpb.getY() + dy);
		}
		purgeRoutingPoints();
		fireWireChanged();
	}

	private void purgeRoutingPoints() {
		if (rps.size() > 0) { /*
							 * Only purge if there are routing points between
							 * the Inport and the Outport.
							 */
			LinkedList<RoutingPoint> delete = new LinkedList<RoutingPoint>();
			RoutingPoint rp1 = op.getPosition(), rp2 = rps.get(0), rp3;
			int i = 1;
			while (i < rps.size()) {
				rp3 = rps.get(i);
				if ((rp1.getX() == rp2.getX() && rp2.getX() == rp3.getX())
						|| (rp1.getY() == rp2.getY() && rp2.getY() == rp3
								.getY())) {
					delete.add(rp2);
				}
				rp1 = rp2;
				rp2 = rp3;
				i++;
			}
			rp3 = ip.getPosition();
			if ((rp1.getX() == rp2.getX() && rp2.getX() == rp3.getX())
					|| (rp1.getY() == rp2.getY() && rp2.getY() == rp3.getY())) {
				delete.add(rp2);
			}
			rps.removeAll(delete);
			setHasChanged();
		}
	}

	protected void adjustForInport() {
		if (rps.isEmpty()) {
			RoutingPoint rp = new RoutingPoint(
					(inrp_old.getX() + outrp_old.getX()) / 2,
					(inrp_old.getY() + outrp_old.getY()) / 2);
			rps.add(new RoutingPoint(rp));
			rps.add(new RoutingPoint(rp));
		}
		WireEdge edge = getRoutingEdges().getLast();
		boolean edge_is_vertical;
		if (edge.isZero()) {
			WireEdge edge_adj = getRoutingEdges().get(0);
			if (edge_adj.isZero() || !edge_adj.isVertical()) {
				edge_is_vertical = true;
			} else {
				edge_is_vertical = false;
			}
		} else {
			edge_is_vertical = edge.isVertical();
		}
		if (edge_is_vertical) {
			edge.move(ip.getPosition().getX() - edge.getP2().getX(), 0);
			edge.getP2().setY(ip.getPosition().getY());
		} else {
			edge.move(0, ip.getPosition().getY() - edge.getP2().getY());
			edge.getP2().setX(ip.getPosition().getX());
		}
		inrp_old = new RoutingPoint(ip.getPosition());
		purgeRoutingPoints();
		fireWireChanged();
	}

	protected void adjustForOutport() {
		if (rps.isEmpty()) {
			RoutingPoint rp = new RoutingPoint(
					(inrp_old.getX() + outrp_old.getX()) / 2,
					(inrp_old.getY() + outrp_old.getY()) / 2);
			rps.add(new RoutingPoint(rp));
			rps.add(new RoutingPoint(rp));
		}
		WireEdge edge = getRoutingEdges().getFirst();
		boolean edge_is_vertical;
		if (edge.isZero()) {
			WireEdge edge_adj = getRoutingEdges().get(1);
			if (edge_adj.isZero() || !edge_adj.isVertical()) {
				edge_is_vertical = true;
			} else {
				edge_is_vertical = false;
			}
		} else {
			edge_is_vertical = edge.isVertical();
		}
		if (edge_is_vertical) {
			edge.move(op.getPosition().getX() - edge.getP1().getX(), 0);
			edge.getP1().setY(op.getPosition().getY());
		} else {
			edge.move(0, op.getPosition().getY() - edge.getP1().getY());
			edge.getP1().setX(op.getPosition().getX());
		}
		outrp_old = new RoutingPoint(op.getPosition());
		purgeRoutingPoints();
		fireWireChanged();
	}

	private void setHasChanged() {
		this.hasChanged = true;
		fireWireChanged();
	}

	private void unsetHasChanged() {
		this.hasChanged = false;
	}
	
	/**
	 * The copied wire's Inport and Outport are null.
	 * 
	 * @return
	 */
	public Wire copy(){
		Wire wire_cp = new Wire();
		LinkedList<RoutingPoint> rps_cp = new LinkedList<RoutingPoint>();
		for (RoutingPoint rp:rps){
			rps_cp.add(new RoutingPoint(rp));
		}
		wire_cp.rps = rps_cp;
		wire_cp.inrp_old = new RoutingPoint(inrp_old);
		wire_cp.outrp_old = new RoutingPoint(outrp_old);
		wire_cp.hasChanged = true;
		return wire_cp;
	}
	
	private void fireWireChanged(){
		CircuitManager.getInstance().getWireManager().update();
	}
}
