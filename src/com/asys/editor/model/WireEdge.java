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
public class WireEdge {
	private final RoutingPoint p1, p2;
	private final Wire parent;

	public WireEdge(RoutingPoint p1, RoutingPoint p2, Wire parent) {
//		assert p1.getX() == p2.getX() || p1.getY() == p2.getY();
		try {
			if(! (p1.getX() == p2.getX() || p1.getY() == p2.getY())){
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("Assertion violated!");
		}

		this.p1 = p1;
		this.p2 = p2;
		this.parent = parent;
	}

	public RoutingPoint getP1() {
		return p1;
	}

	public RoutingPoint getP2() {
		return p2;
	}
	
	public Wire getParent(){
		return parent;
	}

	public boolean isVertical() {
		return p1.getX() == p2.getX();
	}
	
	public boolean isZero(){
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
}
