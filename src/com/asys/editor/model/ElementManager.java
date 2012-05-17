/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import com.asys.model.components.exceptions.DuplicateElementException;
import com.asys.model.components.exceptions.OverlappingElementException;
import com.asys.utils.Decider;

/**
 * @author ryan
 * 
 */
public class ElementManager {
	private CircuitManager cm;
	private ArrayList<Element> elts;
	private boolean hasBoundChanged;
	private Rectangle bound;
	private ElementDictionary dic, dic_exc;

	public ElementManager() {
		elts = new ArrayList<Element>();
		dic = new ElementDictionary(new ArrayList<Element>());
		dic_exc = new ElementDictionary(new ArrayList<Element>());
		bound = new Rectangle();
		dic.build();
		dic_exc.build();
		hasBoundChanged = true;
		// hasElementsChanged = true;
	}

	public void init() {
		this.cm = CircuitManager.getInstance();
		cm.getEdgeManager();
	}
	
	public void clear(){
		elts = new ArrayList<Element>();
		dic = new ElementDictionary(new ArrayList<Element>());
		dic_exc = new ElementDictionary(new ArrayList<Element>());
		bound = new Rectangle();
		dic.build();
		dic_exc.build();
		hasBoundChanged = true;
		// hasElementsChanged = true;
	}

	protected void addElement(Element new_elt)
			throws DuplicateElementException, OverlappingElementException {
		if (!elts.contains(new_elt)) {
			if (hasBoundChanged) {
				getBound();
			}
			if (dic.overlapping(new_elt)) {
				throw new OverlappingElementException();
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
			dic.addToInc(new_elt);
			dic_exc.addToInc(new_elt);
			// hasElementsChanged = true;
		} else {
			throw new DuplicateElementException();
		}
	}

	protected void addElement(List<Element> new_elts) {
		elts.addAll(new_elts);
		dic.addAllToInc(new_elts);
		dic_exc.addAllToInc(new_elts);
		hasBoundChanged = true;
		// hasElementsChanged = true;
	}

	protected boolean remove(Element rem_elt) {
		boolean b = elts.remove(rem_elt);
		if (b) {
			dic.remove(rem_elt);
			dic_exc.remove(rem_elt);
			hasBoundChanged = true;
			// hasElementsChanged = true;
		}
		return b;
	}

	protected boolean remove(List<Element> rem_elts) {
		boolean b = elts.removeAll(rem_elts);
		if (b) {
			dic.removeAll(rem_elts);
			dic_exc.removeAll(rem_elts);
			hasBoundChanged = true;
			// hasElementsChanged = true;
		}
		return b;
	}

	public ArrayList<Element> getElements() {
		return (ArrayList<Element>) elts.clone();
	}

	public Element getElementAt(int x, int y) {
		LinkedList<Element> list = dic.getElementsAtColumn(x);
		Element elt1 = null, elt2 = null;
		if (list != null) {
			for (Element elt : list) {
				if (elt.getY() < y && y < elt.getY() + elt.getHeight()) {
					elt1 = elt;
					break;
				}
			}
		}
		list = dic.getElementsAtRow(y);
		if (list != null) {
			for (Element elt : list) {
				if (elt.getX() < x && x < elt.getX() + elt.getWidth()) {
					elt2 = elt;
					break;
				}
			}
		}
		// If both 'elt1' and 'elt2' are non-null, then they must be equal.
		if (elt1 != null && elt2 != null) {
			assert elt1 == elt2;
			return elt1;
		}
		return null;
	}

	public ArrayList<Element> getElementsInRectangle(Rectangle rec) {
		HashSet<Element> set = new HashSet<Element>();
		int x = Math.min(rec.getX(), rec.getX() + rec.getWidth());
		int y = Math.min(rec.getY(), rec.getY() + rec.getHeight());
		int w = Math.abs(rec.getWidth());
		int h = Math.abs(rec.getHeight());
		for (int i = x; i <= x + w; i++) {
			LinkedList<Element> list = dic.getElementsAtColumn(i);
			if (list != null) {
				for (Element elt : list) {
					if (y < elt.getY() && elt.getY() + elt.getHeight() < y + h) {
						set.add(elt);
					}
				}
			}
		}
		ArrayList<Element> result = new ArrayList<Element>(set);
		return result;
	}

	public ArrayList<Outport> getOutportAt(int x, int y) {
		ArrayList<Outport> ops = new ArrayList<Outport>();
		LinkedList<Element> list = dic.getElementsAtColumn(x);
		if (list != null) {
			for (Element elt : list) {
				if (elt.getY() <= y && y <= elt.getY() + elt.getHeight()) {
					for (Outport op : elt.getOutports()) {
						if (op.getPosition().getX() == x
								&& op.getPosition().getY() == y) {
							ops.add(op);
						}
					}
				}
			}
		}
		return ops;
	}

	public Inport getInportAt(int x, int y) {
		LinkedList<Element> list = dic.getElementsAtColumn(x);
		if (list != null) {
			for (Element elt : list) {
				if (elt.getY() <= y && y <= elt.getY() + elt.getHeight()) {
					for (Inport ip : elt.getInports()) {
						if (ip.getPosition().getX() == x
								&& ip.getPosition().getY() == y) {
							return ip;
						}
					}
				}
			}
		}
		return null;
	}

	public ElementDictionary getElementDictionary() {
		return dic;
	}

	public ElementDictionary getExcludeElementDictionary() {
		return dic_exc;
	}

	protected void update() {
		hasBoundChanged = true;
		// hasElementsChanged = true;
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

	/**
	 * The dictionary indexes the elements vertically and horizontally for fast
	 * retrieval.
	 * 
	 * @author ryan
	 * 
	 */
	class ElementDictionary {
		private HashMap<Integer, LinkedList<Element>> v_map, h_map;
		private ArrayList<Element> inc, exc;
		private boolean hasExcludeChanged;

		ElementDictionary(ArrayList<Element> exclude) {
			v_map = new HashMap<Integer, LinkedList<Element>>();
			h_map = new HashMap<Integer, LinkedList<Element>>();
			this.inc = new ArrayList<Element>();
			this.exc = exclude;
			hasExcludeChanged = true;
		}

		private void build() {
			v_map.clear();
			h_map.clear();
			ArrayList<Element> all = getElements();
			all.removeAll(exc);
			addAllToInc(all);
			for (Element elt : all) {
				subAdd(elt, elt.getX(), elt.getY(), elt.getWidth(),
						elt.getHeight());
				inc.add(elt);
			}
			hasExcludeChanged = false;
		}

		public void addToExc(Element elt) {
			if (!inc.contains(elt) && !exc.contains(elt)) {
				exc.add(elt);
			}
		}

		public void addAllToExc(List<Element> elts) {
			for (Element elt : elts) {
				addToExc(elt);
			}
		}

		public void addToInc(Element elt) {
			if (!inc.contains(elt) && !exc.contains(elt)) {
				subAdd(elt, elt.getX(), elt.getY(), elt.getWidth(),
						elt.getHeight());
				inc.add(elt);
			}
		}

		public void addAllToInc(List<Element> elts) {
			for (Element elt : elts) {
				addToInc(elt);
			}
		}

		public void include(Element elt) {
			assert (!inc.contains(elt) && exc.contains(elt))
					|| (inc.contains(elt) && !exc.contains(elt));
			if (exc.contains(elt)) {
				exc.remove(elt);
				subAdd(elt, elt.getX(), elt.getY(), elt.getWidth(),
						elt.getHeight());
				inc.add(elt);
			}
		}

		public void includeAll(List<Element> elts) {
			for (Element elt : elts) {
				include(elt);
			}
		}

		public void includeAll() {
			includeAll((ArrayList<Element>) exc.clone());
			exc.clear();
		}

		public void exclude(Element elt) {
			assert (!inc.contains(elt) && exc.contains(elt))
					|| (inc.contains(elt) && !exc.contains(elt));
			if (inc.contains(elt)) {
				inc.remove(elt);
				subRemove(elt, elt.getX(), elt.getY(), elt.getWidth(),
						elt.getHeight());
				exc.add(elt);
			}
		}

		public void excludeAll(List<Element> elts) {
			for (Element elt : elts) {
				exclude(elt);
			}
		}

		public void excludeAll() {
			excludeAll((ArrayList<Element>) inc.clone());
			inc.clear();
		}

		public void remove(Element elt) {
			if (inc.remove(elt)) {
				subRemove(elt, elt.getX(), elt.getY(), elt.getWidth(),
						elt.getHeight());
				assert !exc.contains(elt);
			} else if (exc.remove(elt)) {
				// Do nothing.
			}
		}

		public void removeAll(List<Element> elts) {
			for (Element elt : elts) {
				remove(elt);
			}
		}
		
		public void rotate(Element elt){
			if (inc.contains(elt)){
				subRemove(elt, elt.getX(), elt.getY(), elt.getOldWidth(),
						elt.getOldHeight());
				subAdd(elt, elt.getX(), elt.getY(), elt.getWidth(),
						elt.getHeight());
			}
		}

		public void scale(Element elt) {
			if (inc.contains(elt)) {
				subRemove(elt, elt.getX(), elt.getY(), elt.getOldWidth(),
						elt.getOldHeight());
				subAdd(elt, elt.getX(), elt.getY(), elt.getWidth(),
						elt.getHeight());
			}
		}

		public void move(Element elt) {
			if (inc.contains(elt)) {
				subRemove(elt, elt.getOldX(), elt.getOldY(), elt.getWidth(),
						elt.getHeight());
				subAdd(elt, elt.getX(), elt.getY(), elt.getWidth(),
						elt.getHeight());
			}
		}

		public void moveAll(List<Element> elts) {
			for (Element elt : elts) {
				move(elt);
			}
		}

		private void subAdd(Element elt, int x, int y, int w, int h) {
			for (int i = x; i <= x + w; i++) {
				LinkedList<Element> list = v_map.get(i);
				if (list == null) {
					list = new LinkedList<Element>();
					v_map.put(i, list);
				}
				list.add(elt);
			}
			for (int j = y; j <= y + h; j++) {
				LinkedList<Element> list = h_map.get(j);
				if (list == null) {
					list = new LinkedList<Element>();
					h_map.put(j, list);
				}
				list.add(elt);
			}
		}

		private void subRemove(Element elt, int x, int y, int w, int h) {
			for (int i = x; i <= x + w; i++) {
				LinkedList<Element> list = v_map.get(i);
				assert list != null;
				list.remove(elt);
			}

			for (int j = y; j <= y + h; j++) {
				LinkedList<Element> list = h_map.get(j);
				assert list != null;
				list.remove(elt);
			}
		}

		// public void setExcludeChanged() {
		// hasExcludeChanged = true;
		// }
		//
		// public void setExclude(ArrayList<Element> exc) {
		// includeAll(this.exc);
		// this.inc.addAll(exc);
		// this.exc = exc;
		// hasExcludeChanged = true;
		// }

		// public void update(List<Element> moved_elts) {
		//
		// }

		public LinkedList<Element> getElementsAtRow(int y) {
			if (hasExcludeChanged) {
				build();
				hasExcludeChanged = false;
			}
			return h_map.get(y);
		}

		public LinkedList<Element> getElementsAtColumn(int x) {
			if (hasExcludeChanged) {
				build();
				hasExcludeChanged = false;
			}
			return v_map.get(x);
		}

		/**
		 * 
		 * @param elt
		 * @return true - if 'elt' overlaps any Element in the dictionary
		 *         (includeing the case 'elt' is in the dictionary and is not in
		 *         exclude).
		 */
		public boolean overlapping(Element elt) {
			return overlapping(elt.getX(), elt.getY(), elt.getWidth(),
					elt.getHeight());
		}

		/**
		 * 
		 * @param x
		 * @param y
		 * @param w
		 * @param h
		 * @return
		 */
		public boolean overlapping(int x, int y, int w, int h) {
			if (hasExcludeChanged) {
				build();
				hasExcludeChanged = false;
			}
			if (w <= 1) {
				boolean b1 = true, b2 = true;
				if (overlappingSub(x, y, h, true)) {
					b1 = overlappingSub(x + 1, y, h, true);
					if (w == 0)
						b2 = overlappingSub(x - 1, y, h, true);
					if (b1 && b2)
						return true;
				}
			}
			for (int i = x + 1; i < x + w; i++) {
				if (overlappingSub(i, y, h, true)) {
					return true;
				}
			}
			if (h <= 1) {
				boolean b1 = true, b2 = true;
				if (overlappingSub(y, x, w, false)) {
					b1 = overlappingSub(y + 1, x, w, false);
					if (h == 0)
						b2 = overlappingSub(y - 1, x, w, false);
					if (b1 && b2)
						return true;
				}
			}
			for (int j = y + 1; j < y + h; j++) {
				if (overlappingSub(j, x, w, false)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Decide if the line formed by i, base and span overlaps any Element.
		 * If isVertical == true, then the line is (i, base) (i, base+span); if
		 * isVertical == false, then the line is (base, i), (base+span, i)
		 * 
		 * @param i
		 *            - The coordinate that the end points of the line share.
		 * @param base
		 * @param span
		 * @param isVertical
		 * @return
		 */
		private boolean overlappingSub(int i, int base, int span,
				boolean isVertical) {
			if (isVertical) {
				LinkedList<Element> list = v_map.get(i);
				if (list != null) {
					for (Element tgt : list) {
						int ty1 = tgt.getY();
						int ty2 = ty1 + tgt.getHeight();
						int ey1 = base;
						int ey2 = ey1 + span;
						if (Decider.overlap(ty1, ty2, ey1, ey2, false)) {
							return true;
						}
					}
				}
			} else {
				LinkedList<Element> list = dic_exc.getElementsAtRow(i);
				if (list != null) {
					for (Element tgt : list) {
						int tx1 = tgt.getX();
						int tx2 = tx1 + tgt.getWidth();
						int ex1 = base;
						int ex2 = ex1 + span;
						if (Decider.overlap(tx1, tx2, ex1, ex2, false)) {
							return true;
						}
					}
				}
			}
			return false;
		}
	}
}
