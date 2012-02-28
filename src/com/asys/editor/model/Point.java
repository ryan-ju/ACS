/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 * 
 */
public class Point {
	private int x, y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point p) {
		setPosition(p.getX(), p.getY());
	}

	public int getX() {
		return x;
	}

	protected void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	protected void setY(int y) {
		this.y = y;
	}

	protected void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public boolean overlap(Point other) {
		return x == other.getX() && y == other.getY();
	}

	public static boolean overlap(Point p1, Point p2) {
		return p1.getX() == p2.getX() && p1.getY() == p2.getY();
	}

	protected void move(int dx, int dy) {
		x += dx;
		y += dy;
	}
}
