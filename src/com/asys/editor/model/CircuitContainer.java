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
public class CircuitContainer {
	private ArrayList<Element> elts;
	private ArrayList<Wire> wires;
	
	public CircuitContainer(){
		elts = new ArrayList<Element>();
		wires = new ArrayList<Wire>();
	}
	
	public CircuitContainer(List<Element> input_elts, List<Wire> input_wires){
		elts = new ArrayList<Element>();
		wires = new ArrayList<Wire>();
		setElements(input_elts);
		setWires(input_wires);
	}
	
	public ArrayList<Element> getElements() {
		return elts;
	}
	
	public void setElements(List<Element> input_elts){
		elts.clear();
		for (Element elt:input_elts){
			elts.add(elt);
		}
	}
	
	public ArrayList<Wire> getWires(){
		return wires;
	}
	
	public void setWires(List<Wire> input_wires){
		wires.clear();
		for (Wire wire:input_wires){
			wires.add(wire);
		}
	}
}
