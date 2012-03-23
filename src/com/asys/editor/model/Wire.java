/**
 * 
 */
package com.asys.editor.model;

import java.util.LinkedList;
import java.util.List;

import com.asys.constants.Direction;
import com.asys.model.components.exceptions.InvalidRoutingPointException;

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

	private Wire() {
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
		purgeRoutingPoints();
		setHasChanged();
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
//		setHasChanged();
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
//		setHasChanged();
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
	 * Adds a pair of routing points to the wire edge with index 'index'. The
	 * newly created wire edge will be shifted orthogonally by 1 unit.
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
					rps.add(index, new RoutingPoint(min_ex, max_y));
					rps.add(index, new RoutingPoint(min_ex, max_y));
					rps.add(index, new RoutingPoint(min_ex, min_y));
					rps.add(index, new RoutingPoint(min_ex, min_y));
				} else { // 'edge' points upwards
					assert edge.getDirection() == Direction.UP;
					rps.add(index, new RoutingPoint(min_ex, min_y));
					rps.add(index, new RoutingPoint(min_ex, min_y));
					rps.add(index, new RoutingPoint(min_ex, max_y));
					rps.add(index, new RoutingPoint(min_ex, max_y));
				}
				setHasChanged();
				moveEdge(index + 2, 1, 0);
//				fireWireChanged();
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
					rps.add(index, new RoutingPoint(max_x, min_ey));
					rps.add(index, new RoutingPoint(max_x, min_ey));
					rps.add(index, new RoutingPoint(min_x, min_ey));
					rps.add(index, new RoutingPoint(min_x, min_ey));
				} else { // 'edge' points left
					assert edge.getDirection() == Direction.LEFT;
					rps.add(index, new RoutingPoint(min_x, min_ey));
					rps.add(index, new RoutingPoint(min_x, min_ey));
					rps.add(index, new RoutingPoint(max_x, min_ey));
					rps.add(index, new RoutingPoint(max_x, min_ey));
				}
				setHasChanged();
				moveEdge(index + 2, 0, 1);
//				fireWireChanged();
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
			try {
				res = new LinkedList<WireEdge>();
				int i = 0;
				// RoutingPoint rpp = op.getPosition();
				RoutingPoint rpp = outrp_old;
				for (RoutingPoint rp : rps) {
					res.addLast(new WireEdge(rpp, rp, this, i));
					rpp = rp;
					i++;
				}
				// res.add(new WireEdge(rpp, ip.getPosition()));
				res.add(new WireEdge(rpp, inrp_old, this, i));
				unsetHasChanged();
			} catch (InvalidRoutingPointException e) {
				e.printStackTrace();
			}
		}
		return res;
	}

	public boolean isOn(int x, int y) {
		for (WireEdge edge : getRoutingEdges()) {
			if (WireEdge.isOnWireEdge(new Point(x, y), edge)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return -1 - (x, y) is not on the Wire.
	 */
	public int getWireEdgeIndexAt(int x, int y) {
		int i = 0;
		for (WireEdge edge : getRoutingEdges()) {
			if (WireEdge.isOnWireEdge(new Point(x, y), edge)) {
				return i;
			}
			i++;
		}
		return -1;
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
//		fireWireChanged();
	}

	/**
	 * 
	 * @param index
	 * @param dx
	 * @param dy
	 * @return true - if wire edges were added or removed.
	 */
	protected boolean moveEdge(int index, int dx, int dy) {
		assert index >= 0 && index < getRoutingEdges().size();
		boolean f = false;
		WireEdge edge = getRoutingEdges().get(index);
		int size = getRoutingEdges().size();
		// If 'edge' is the first edge
		if (index == 0) {
			addRoutingPointAtEnd(true);
			setHasChanged();
			edge = getRoutingEdges().get(1);
			f = true;
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
			f = true;
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
		f = purgeRoutingPoints() ? true : f;
		setHasChanged();
		return f;
	}

	/**
	 * 
	 * @return true - if any routing points have been deleted.
	 */
	private boolean purgeRoutingPoints() {
		boolean hasDeleted = false;
		while (purgeRoutingPointsSub()) {
			hasDeleted = true;
		}
		return hasDeleted;
	}

	/*
	 * Returns true if there are points deleted.
	 */
	private boolean purgeRoutingPointsSub() {
		boolean hasDeleted = false;
		if (rps.size() > 0) { /*
							 * Only purge if there are routing points between
							 * the Inport and the Outport.
							 */
			LinkedList<RoutingPoint> delete = new LinkedList<RoutingPoint>();
			RoutingPoint rp1 = outrp_old, rp2 = rps.get(0), rp3;
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
			rp3 = inrp_old;
			if ((rp1.getX() == rp2.getX() && rp2.getX() == rp3.getX())
					|| (rp1.getY() == rp2.getY() && rp2.getY() == rp3.getY())) {
				delete.add(rp2);
			}
			hasDeleted = !delete.isEmpty();
			rps.removeAll(delete);
			setHasChanged();
		}
		return hasDeleted;
	}

	protected void adjustForInport() {
		if (rps.isEmpty()) {
			RoutingPoint rp = new RoutingPoint(
					(inrp_old.getX() + outrp_old.getX()) / 2,
					(inrp_old.getY() + outrp_old.getY()) / 2);
			rps.add(new RoutingPoint(rp));
			rps.add(new RoutingPoint(rp));
		}
		WireEdge edge = null;
		try {
			edge = new WireEdge(rps.getLast(), inrp_old, this, rps.size());
		} catch (InvalidRoutingPointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert edge != null;
		boolean edge_is_vertical;
		// If 'edge' has zero length, then it is due to the two overlapping
		// routing points being added to the empty 'rps'
		if (edge.isZero()) {
			assert rps.size() == 2;
			WireEdge edge_first = null;
			try {
				edge_first = new WireEdge(outrp_old, rps.getFirst(), this, 0);
			} catch (InvalidRoutingPointException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assert edge_first != null;
			edge_is_vertical = edge_first.isVertical();
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
//		fireWireChanged();
	}

	protected void adjustForOutport() {
		if (rps.isEmpty()) {
			RoutingPoint rp = new RoutingPoint(
					(inrp_old.getX() + outrp_old.getX()) / 2,
					(inrp_old.getY() + outrp_old.getY()) / 2);
			rps.add(new RoutingPoint(rp));
			rps.add(new RoutingPoint(rp));
		}
		WireEdge edge = null;
		try {
			edge = new WireEdge(outrp_old, rps.getFirst(), this, 0);
		} catch (InvalidRoutingPointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assert edge != null;
		boolean edge_is_vertical;
		// If 'edge' has zero length, then it is due to the two overlapping
		// routing points being added to the empty 'rps'
		if (edge.isZero()) {
			assert rps.size() == 2;
			WireEdge edge_last = null;
			try {
				edge_last = new WireEdge(rps.getLast(), inrp_old, this,
						rps.size());
			} catch (InvalidRoutingPointException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			assert edge_last != null;
			edge_is_vertical = edge_last.isVertical();
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
//		fireWireChanged();
	}

	private void setHasChanged() {
		this.hasChanged = true;
//		fireWireChanged();
	}

	private void unsetHasChanged() {
		this.hasChanged = false;
	}

	/**
	 * The copied wire's Inport and Outport are null.
	 * 
	 * @return
	 */
	public Wire copy() {
		Wire wire_cp = new Wire();
		LinkedList<RoutingPoint> rps_cp = new LinkedList<RoutingPoint>();
		for (RoutingPoint rp : rps) {
			rps_cp.add(new RoutingPoint(rp));
		}
		wire_cp.rps = rps_cp;
		wire_cp.inrp_old = new RoutingPoint(inrp_old);
		wire_cp.outrp_old = new RoutingPoint(outrp_old);
		wire_cp.hasChanged = true;
		return wire_cp;
	}

	public WireState exportState() {
		WireState state = new WireState();
		state.setIp(ip);
		state.setOp(op);
		state.setRps(rps);
		return state;
	}

	private void importState(WireState state) {
		state.getIp().setWire(this);
		state.getOp().setWire(this);
		inrp_old = new RoutingPoint(state.getIp().getPosition());
		outrp_old = new RoutingPoint(state.getOp().getPosition());
		this.setRoutingPoints(state.getRps());
	}

//	private void fireWireChanged() {
//		CircuitManager.getInstance().getWireManager().update();
//		CircuitManager.getInstance().getEdgeManager().update();
//		CircuitManager.getInstance().fireStateChangedEvent();
//	}

	public static void validateWireEdges(Outport op, Inport ip,
			List<RoutingPoint> rps) throws InvalidRoutingPointException {
		RoutingPoint rpp = op.getPosition();
		for (RoutingPoint rp : rps) {
			new WireEdge(rpp, rp, null, -1);
			rpp = rp;
		}
		new WireEdge(rpp, ip.getPosition(), null, -1);
	}

	class WireState {
		private Inport ip;
		private Outport op;
		private LinkedList<RoutingPoint> rps = new LinkedList<RoutingPoint>();

		public Inport getIp() {
			return this.ip;
		}

		public void setIp(Inport ip) {
			this.ip = ip;
		}

		public Outport getOp() {
			return this.op;
		}

		public void setOp(Outport op) {
			this.op = op;
		}

		public LinkedList<RoutingPoint> getRps() {
			return (LinkedList<RoutingPoint>) this.rps.clone();
		}

		public void setRps(LinkedList<RoutingPoint> rps) {
			rps = new LinkedList<RoutingPoint>();
			for (RoutingPoint rp : Wire.this.rps) {
				this.rps.addLast(new RoutingPoint(rp));
			}
		}

		public void restore() {
			Wire.this.importState(this);
		}
	}
}
