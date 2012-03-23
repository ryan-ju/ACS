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
public class ClipBoard{
	private static ClipBoard cb;
	private ArrayList<Element> elts;
	private ArrayList<Wire> wires;
	private boolean isEmpty;
	
	private ClipBoard(){
		elts = new ArrayList<Element>();
		wires = new ArrayList<Wire>();
		isEmpty = true;
	}
	
	public static ClipBoard getInstance(){
		if (cb == null) cb = new ClipBoard();
		return cb;
	}
	
	public List<Element> getElements(){
		return elts;
	}
	
	protected void setElements(ArrayList<Element> elts){
		this.elts = elts;
		isEmpty = false;
	}
	
	public List<Wire> getWires(){
		return wires;
	}
	
	protected void setWires(ArrayList<Wire> wires){
		this.wires = wires;
	}
	
	public void clear(){
		this.elts.clear();
		this.wires.clear();
		isEmpty = true;
	}
	
	public boolean isEmpty(){
		return isEmpty;
	}
	
	public CircuitContainer getNewCircuit(){
		return CircuitUtilities.copyCircuit(elts);
	}
}
