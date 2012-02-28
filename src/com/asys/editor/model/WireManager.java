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
	private static CircuitManager cm;
	private ArrayList<Wire> wires;
	private static EdgeManager edgm;

	public WireManager() {
		this.wires = new ArrayList<Wire>();
		
	}
	
	public void init(){
		cm = CircuitManager.getInstance();
		edgm = cm.getEdgeManager();
	}
	
	protected void addWire(Wire new_wire){
		wires.add(new_wire);
		update();
	}
	
	protected void addWire(List<Wire> new_wires){
		wires.addAll(new_wires);
		update();
	}
	
	protected boolean remove(Wire rem_wire){
		boolean b = wires.remove(rem_wire);
		if (b) update();
		return b;
	}
	
	protected boolean remove(List<Wire> rem_wires){
		boolean b = wires.removeAll(rem_wires);
		if (b) update();
		return b;
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
	protected Wire getWireAt(int x, int y){
		return edgm.getWireAt(x, y);
	}
	
	protected void update(){
		cm.setWireManagerChanged();
	}
}
