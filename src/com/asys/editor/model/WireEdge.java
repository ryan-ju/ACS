/**
 * 
 */
package com.asys.editor.model;

import com.asys.constants.Direction;

/**
 * The edge is vertical or horizontal.
 * 
 * @author ryan
 * 
 */
public class WireEdge extends Edge {
	private final Wire parent;

	public WireEdge(RoutingPoint p1, RoutingPoint p2, Wire parent) {
		super(p1, p2);
		// assert p1.getX() == p2.getX() || p1.getY() == p2.getY();
		try {
			if (!(p1.getX() == p2.getX() || p1.getY() == p2.getY())) {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("Assertion violated!");
		}
		this.parent = parent;
	}

	public Wire getParent() {
		return parent;
	}

	public boolean isVertical() {
		return p1.getX() == p2.getX();
	}

	public boolean isZero() {
		return p1.getX() == p2.getX() && p1.getY() == p2.getY();
	}

	public Direction getDirection() {
		if (isVertical()) {
			if (p1.getY() > p2.getY()) {
				return Direction.UP;
			} else {
				return Direction.DOWN;
			}
		} else {
			if (p1.getX() > p2.getX()) {
				return Direction.LEFT;
			} else {
				return Direction.RIGHT;
			}
		}
	}

	@Override
	/**
	 * Can only move orthogonally.
	 * 
	 * @param dx
	 * @param dy
	 */
	protected void move(int dx, int dy) {
		if (isVertical()) {
			p1.move(dx, 0);
			p2.move(dx, 0);
		} else {
			p1.move(0, dy);
			p2.move(0, dy);
		}
	}
	
	public static Edge getOverlap(WireEdge edge, Element elt){
		RoutingPoint rp1 = edge.getP1();
		RoutingPoint rp2 = edge.getP2();
		if (edge.isVertical()){
			int x = edge.getP1().getX();
			if (elt.getX() < x && x< elt.getX()+elt.getWidth()){
				int edge_min_y = Math.min(rp1.getY(), rp2.getY());
				int edge_max_y = Math.max(rp1.getY(), rp2.getY());
				int elt_min_y = elt.getY();
				int elt_max_y = elt_min_y+elt.getHeight();
				// This is the condition for overlapping.
				if (edge_min_y < elt_max_y && elt_min_y < edge_max_y) {
					int max_min_y = Math.max(edge_min_y, elt_min_y);
					int min_max_y = Math.min(edge_max_y, elt_max_y);
					return new Edge(
							new RoutingPoint(x, min_max_y),
							new RoutingPoint(x, max_min_y));
				}
			}
		}else{
			int y = edge.getP1().getY();
			if (elt.getY()<y && y<elt.getY()+elt.getHeight()){
				int edge_min_x = Math.min(rp1.getX(), rp2.getX());
				int edge_max_x = Math.max(rp1.getX(), rp2.getX());
				int elt_min_x = elt.getX();
				int elt_max_x = elt_min_x+elt.getWidth();
				// This is the condition for overlapping.
				if (edge_min_x < elt_max_x && elt_min_x < edge_max_x) {
					int max_min_x = Math.max(edge_min_x, elt_min_x);
					int min_max_x = Math.min(edge_max_x, elt_max_x);
					return new Edge(
							new RoutingPoint(min_max_x, y),
							new RoutingPoint(max_min_x, y));
				}
			}
		}
		return null;
	}

	public static Edge getOverlap(WireEdge e1, WireEdge e2) {
		RoutingPoint e1p1 = e1.getP1();
		RoutingPoint e1p2 = e1.getP2();
		RoutingPoint e2p1 = e2.getP1();
		RoutingPoint e2p2 = e2.getP2();
		if (e1.isVertical() != e2.isVertical()) { // The two wires are
													// orthogonal
			if (isOnWireEdge(e1p1, e2)) {
				return new Edge(new RoutingPoint(e1p1), new RoutingPoint(e1p1));
			} else if (isOnWireEdge(e1p2, e2)) {
				return new Edge(new RoutingPoint(e1p2), new RoutingPoint(e1p2));
			} else if (isOnWireEdge(e2p1, e1)) {
				return new Edge(new RoutingPoint(e2p1), new RoutingPoint(e2p1));
			} else if (isOnWireEdge(e2p2, e1)) {
				return new Edge(new RoutingPoint(e2p2), new RoutingPoint(e2p2));
			}
		} else { // The two wires are parallel
			if (e1.isVertical()) {
				assert e2.isVertical();
				if (e1p1.getX() == e2p1.getX()) { // e1 and e2 has the same
													// x-coordinates
					int e1_min_y = Math.min(e1p1.getY(), e1p2.getY());
					int e1_max_y = Math.max(e1p1.getY(), e1p2.getY());
					int e2_min_y = Math.min(e2p1.getY(), e2p2.getY());
					int e2_max_y = Math.max(e2p1.getY(), e2p2.getY());
					// This is the condition for overlapping.
					if (e1_min_y <= e2_max_y && e2_min_y <= e1_max_y) {
						int max_min_y = Math.max(e1_min_y, e2_min_y);
						int min_max_y = Math.min(e1_max_y, e2_max_y);
						return new Edge(
								new RoutingPoint(e1p1.getX(), min_max_y),
								new RoutingPoint(e1p1.getX(), max_min_y));
					}
				}
			} else {
				assert !e1.isVertical();
				if (e1p1.getY() == e2p1.getY()) {
					int e1_min_x = Math.min(e1p1.getX(), e1p2.getX());
					int e1_max_x = Math.max(e1p1.getX(), e1p2.getX());
					int e2_min_x = Math.min(e2p1.getX(), e2p2.getX());
					int e2_max_x = Math.max(e2p1.getX(), e2p2.getX());
					if (e1_min_x <= e2_max_x && e2_min_x <= e1_max_x) {
						int max_min_x = Math.max(e1_min_x, e2_min_x);
						int min_max_x = Math.min(e1_max_x, e2_max_x);
						return new Edge(
								new RoutingPoint(min_max_x, e1p1.getY()),
								new RoutingPoint(max_min_x, e1p1.getY()));
					}
				}
			}
		}
		return null;

	}

	private static boolean isOnWireEdge(RoutingPoint p, WireEdge edge) {
		RoutingPoint p1 = edge.getP1();
		RoutingPoint p2 = edge.getP2();
		int min_x = Math.min(p1.getX(), p2.getX());
		int max_x = Math.max(p1.getX(), p2.getX());
		int min_y = Math.min(p1.getY(), p2.getY());
		int max_y = Math.max(p1.getY(), p2.getY());
		if (edge.isVertical()) {
			return p.getX() == p1.getX() && min_y <= p.getY()
					&& p.getY() <= max_y;
		} else {
			return p.getY() == p1.getY() && min_x <= p.getX()
					&& p.getX() <= max_x;
		}
	}
}
