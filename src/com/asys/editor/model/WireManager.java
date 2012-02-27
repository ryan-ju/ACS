/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ryan
 *
 */
public class WireManager {
	private CircuitManager cm;
	private ArrayList<Wire> wires;
	private EdgeManager edgm;

	public WireManager(CircuitManager cm) {
		this.cm = cm;
		this.wires = new ArrayList<Wire>();
		this.edgm = new EdgeManager(this);
	}
	
	public EdgeManager getEdgeManager(){
		return edgm;
	}
	
	protected void addWire(Wire new_wire){
		wires.add(new_wire);
	}
	
	protected void addWire(List<Wire> new_wires){
		wires.addAll(new_wires);
	}
	
	protected boolean remove(Wire rem_wire){
		return wires.remove(rem_wire);
	}
	
	protected boolean remove(List<Wire> rem_wires){
		return wires.removeAll(rem_wires);
	}
	
	public ArrayList<Wire> getWires(){
		return (ArrayList<Wire>) wires.clone();
	}
	
	/**
	 * This method is only called by selection manager to decide 
	 * @param x
	 * @param y
	 * @return
	 */
	protected Wire wireAt(int x, int y){
		
	}
}
