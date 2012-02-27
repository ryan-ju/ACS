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
public class ElementManager{
	private CircuitManager parent;
	private ArrayList<Element> elts;
	
	public ElementManager(CircuitManager parent) {
		this.parent = parent;
		elts = new ArrayList<Element>();
	}
	
	protected void addElement(Element new_elt){
		elts.add(new_elt);
	}
	
	protected void addElement(List<Element> new_elts){
		elts.addAll(new_elts);
	}
	
	protected boolean remove(Element rem_elt){
		return elts.remove(rem_elt);
	}
	
	protected boolean remove(List<Element> rem_elts){
		return elts.removeAll(rem_elts);
	}
	
	public ArrayList<Element> getElements(){
		return (ArrayList<Element>) elts.clone();
	}
		
}
