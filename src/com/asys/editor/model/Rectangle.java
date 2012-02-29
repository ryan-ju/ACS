/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 * 
 */
public class Rectangle {
	private int x, y, w, h;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public Point getPosition() {
		return new Point(x, y);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(Point p) {
		this.x = p.getX();
		this.y = p.getY();
	}
}
