/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ryan
 * 
 */
public class ElementManager {
	private CircuitManager cm;
	private EdgeManager edgm;
	private ArrayList<Element> elts;
	private boolean hasBoundChanged, hasElementsChanged;
	private Rectangle bound;
	private ElementDictionary dic;

	public ElementManager() {
		elts = new ArrayList<Element>();
		dic = new ElementDictionary();
		bound = new Rectangle();
		hasBoundChanged = true;
		hasElementsChanged = true;
	}
	
	public void init(){
		this.cm = CircuitManager.getInstance();
		this.edgm = cm.getEdgeManager();
	}

	protected void addElement(Element new_elt) {
		if (hasBoundChanged){
			getBound();
		}
		elts.add(new_elt);
		int x1 = new_elt.getX();
		int y1 = new_elt.getY();
		int x2 = x1 + new_elt.getWidth();
		int y2 = y1 + new_elt.getHeight();
		int bx1 = bound.getX();
		int by1 = bound.getY();
		int bx2 = bx1 + bound.getWidth();
		int by2 = by1 + bound.getHeight();
		if (x1 < bx1)
			bound.setX(x1);
		if (y1 < by1)
			bound.setY(y1);
		if (x2 > bx2)
			bound.setWidth(x2 - bx1);
		if (y2 > by2)
			bound.setHeight(y2 - by1);
		hasElementsChanged = true;
	}

	protected void addElement(List<Element> new_elts) {
		elts.addAll(new_elts);
		hasBoundChanged = true;
		hasElementsChanged = true;
	}

	protected boolean remove(Element rem_elt) {
		boolean b = elts.remove(rem_elt);
		if (b) {
			hasBoundChanged = true;
			hasElementsChanged = true;
		}
		return b;
	}

	protected boolean remove(List<Element> rem_elts) {
		boolean b = elts.removeAll(rem_elts);
		if (b) {
			hasBoundChanged = true;
			hasElementsChanged = true;
		}
		return b;
	}

	public ArrayList<Element> getElements() {
		return (ArrayList<Element>) elts.clone();
	}
	
	public ElementDictionary getElementDictionary(){
		return dic;
	}

	protected void update() {
		hasBoundChanged = true;
		hasElementsChanged = true;
		cm.setElementManagerChanged();
	}

	public Rectangle getBound() {
		if (hasBoundChanged) {
			int x1 = Integer.MAX_VALUE, y1 = Integer.MAX_VALUE, x2 = Integer.MIN_VALUE, y2 = Integer.MIN_VALUE;
			for (Element elt : getElements()) {
				int ex1 = elt.getX();
				int ey1 = elt.getY();
				int ex2 = ex1 + elt.getWidth();
				int ey2 = ey1 + elt.getHeight();
				if (ex1 < x1)
					x1 = ex1;
				if (ey1 < y1)
					y1 = ey1;
				if (ex2 > x2)
					x2 = ex2;
				if (ey2 > y2)
					y2 = ey2;
			}
			bound.setX(x1);
			bound.setY(y1);
			bound.setWidth(x2 - x1);
			bound.setHeight(y2 - y1);
			hasBoundChanged = false;
		}
		return bound;
	}

	class ElementDictionary {
		private HashMap<Integer, LinkedList<Element>> v_map, h_map;

		ElementDictionary() {
			v_map = new HashMap<Integer, LinkedList<Element>>();
			h_map = new HashMap<Integer, LinkedList<Element>>();
		}

		private void build() {
			v_map.clear();
			h_map.clear();
			for (Element elt : getElements()) {
				for (int i = elt.getX(); i <= elt.getX() + elt.getWidth(); i++) {
					LinkedList<Element> list = v_map.get(i);
					if (list == null) {
						list = new LinkedList<Element>();
						v_map.put(i, list);
					}
					list.add(elt);
				}
				for (int j = elt.getY(); j <= elt.getY() + elt.getHeight(); j++) {
					LinkedList<Element> list = h_map.get(j);
					if (list == null) {
						list = new LinkedList<Element>();
						h_map.put(j, list);
					}
					list.add(elt);
				}
			}
		}

		public LinkedList<Element> getElementsAtRow(int y) {
			if (hasElementsChanged) {
				build();
				hasElementsChanged = false;
			}
			return h_map.get(y);
		}

		public LinkedList<Element> getElementsAtColumn(int x) {
			if (hasElementsChanged) {
				build();
				hasElementsChanged = false;
			}
			return v_map.get(x);
		}

	}
}
