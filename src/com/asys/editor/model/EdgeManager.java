package com.asys.editor.model;

import java.util.HashMap;

public class EdgeManager {
	private WireManager cm;
	private HashMap<Integer, LinkedList<WireEdge>> h_edges, v_edges;
	
	public EdgeManager(WireManager cm) {
		this.cm = cm;
		h_edges = new HashMap<Integer, LinkedList<WireEdge> >();
		v_edges = new HashMap<Integer, WireEdge>();
	}
	
	protected void addWire(Wire wire){
		
	}
}
