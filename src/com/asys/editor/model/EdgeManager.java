package com.asys.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.asys.editor.model.ElementManager.ElementDictionary;

public class EdgeManager {
	private WireManager wm;
	private ElementManager em;
	private HashMap<Integer, ArrayList<WireEdge>> h_edges, v_edges;
	// private final HashMap<Integer, ArrayList<RoutingPoint>> h_ps, v_ps;
	private ArrayList<Edge> overlap;
	private boolean needToBuild, needToRenewOverlap;

	public EdgeManager() {
		h_edges = new HashMap<Integer, ArrayList<WireEdge>>();
		v_edges = new HashMap<Integer, ArrayList<WireEdge>>();
		// h_ps = new HashMap<Integer, ArrayList<RoutingPoint>>();
		// v_ps = new HashMap<Integer, ArrayList<RoutingPoint>>();
		overlap = new ArrayList<Edge>();
		needToBuild = true;
		needToRenewOverlap = true;
	}

	public void init() {
		this.wm = CircuitManager.getInstance().getWireManager();
		this.em = CircuitManager.getInstance().getElementManager();
	}
	
	public void clear(){
		h_edges = new HashMap<Integer, ArrayList<WireEdge>>();
		v_edges = new HashMap<Integer, ArrayList<WireEdge>>();
		// h_ps = new HashMap<Integer, ArrayList<RoutingPoint>>();
		// v_ps = new HashMap<Integer, ArrayList<RoutingPoint>>();
		overlap = new ArrayList<Edge>();
		needToBuild = true;
		needToRenewOverlap = true;
	}

	protected void build() {
		h_edges.clear();
		v_edges.clear();
		for (Wire wire : wm.getWires()) {
			subAddWire(wire);
		}
		needToBuild = false;
		needToRenewOverlap = true;
	}

	private void subAddWire(Wire wire) {
		for (WireEdge edge : wire.getRoutingEdges()) {
			if (edge.isVertical()) {
				ArrayList<WireEdge> list = v_edges.get(edge.getP1().getX());
				if (list == null) {
					list = new ArrayList<WireEdge>();
					v_edges.put(edge.getP1().getX(), list);
				}
				v_edges.get(edge.getP1().getX()).add(edge);
			} else {
				ArrayList<WireEdge> list = h_edges.get(edge.getP1().getY());
				if (list == null) {
					list = new ArrayList<WireEdge>();
					h_edges.put(edge.getP1().getY(), list);
				}
				h_edges.get(edge.getP1().getY()).add(edge);
			}
		}
	}

	/**
	 * 
	 * @return An ArrayList of Edges representing the places of overlapping.
	 *         Note the Edges are not RoutingEdges.
	 */
	public ArrayList<Edge> getOverlapping() {
		if (needToRenewOverlap) {
			overlap.clear();
			build();
			// Overlapping between Wires
			for (ArrayList<WireEdge> list : v_edges.values()) {
				for (int i = 0; i < list.size(); i++) {
					for (int j = i + 1; j < list.size(); j++) {
						WireEdge e1 = list.get(i);
						WireEdge e2 = list.get(j);
						Edge overlap_edge = WireEdge.getOverlap(e1, e2);
						if (overlap_edge != null) {
							overlap.add(overlap_edge);
						}
					}

				}
			}
			for (ArrayList<WireEdge> list : h_edges.values()) {
				for (int i = 0; i < list.size(); i++) {
					for (int j = i + 1; j < list.size(); j++) {
						WireEdge e1 = list.get(i);
						WireEdge e2 = list.get(j);
						Edge overlap_edge = WireEdge.getOverlap(e1, e2);
						if (overlap_edge != null) {
							overlap.add(overlap_edge);
						}
					}
				}
			}
			ElementDictionary dic = em.getElementDictionary();
			// Overlapping between Wires and Elements
			for (Integer key : v_edges.keySet()) {
				for (WireEdge edge : v_edges.get(key)) {
					LinkedList<Element> list = dic.getElementsAtColumn(key);
					if (list != null) {
						for (Element elt : list) {
							Edge overlap_edge = WireEdge.getOverlap(edge, elt);
							if (overlap_edge != null) {
								overlap.add(overlap_edge);
							}
						}
					}
				}
			}
			for (Integer key : h_edges.keySet()) {
				for (WireEdge edge : h_edges.get(key)) {
					LinkedList<Element> list = dic.getElementsAtRow(key);
					if (list != null) {
						for (Element elt : list) {
							Edge overlap_edge = WireEdge.getOverlap(edge, elt);
							if (overlap_edge != null) {
								overlap.add(overlap_edge);
							}
						}
					}
				}
			}
			needToRenewOverlap = false;
		}
		return overlap;
	}

	public WireEdge getEdgeAt(int x, int y) {
		if (needToBuild) {
			build();
		}
		ArrayList<WireEdge> list = v_edges.get(x);
		if (list != null) {
			for (WireEdge edge : list) {
				int min_y = Math.min(edge.getP1().getY(), edge.getP2().getY());
				int max_y = Math.max(edge.getP1().getY(), edge.getP2().getY());
				if (min_y <= y && y < max_y) {
					return edge;
				}
			}
		}
		list = h_edges.get(y);
		if (list != null) {
			for (WireEdge edge : list) {
				int min_x = Math.min(edge.getP1().getX(), edge.getP2().getX());
				int max_x = Math.max(edge.getP1().getX(), edge.getP2().getX());
				if (min_x <= x && x < max_x) {
					return edge;
				}
			}
		}
		return null;
	}

	protected void update() {
		needToBuild = true;
		needToRenewOverlap = true;
	}

	protected void needToBuild() {
		needToBuild = true;
	}

	protected void needToRenewOverlap() {
		needToRenewOverlap = true;
	}
}
