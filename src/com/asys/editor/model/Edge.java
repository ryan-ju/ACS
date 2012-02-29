/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 * 
 */
public class Edge {
	protected final RoutingPoint p1, p2;

	public Edge(RoutingPoint p1, RoutingPoint p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	public RoutingPoint getP1() {
		return p1;
	}

	public RoutingPoint getP2() {
		return p2;
	}
	
	protected void move(int dx, int dy) {
		p1.move(dx, dy);
		p2.move(dx, dy);
	}
}
