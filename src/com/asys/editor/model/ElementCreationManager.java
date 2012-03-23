/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;

import com.asys.editor.model.ElementManager.ElementDictionary;

/**
 * @author ryan
 *
 */
public class ElementCreationManager {
	private static ElementCreationManager instance;
	private CircuitManager cm;
	private ArrayList<ElementCreationManagerListener> listeners;
	private Element elt;
	private int x, y;
	
	public static ElementCreationManager getInstance(){
		if (instance == null){
			instance = new ElementCreationManager();
		}
		return instance;
	}
	
	private ElementCreationManager(){
		this.listeners  = new ArrayList<ElementCreationManagerListener>();
		this.cm = CircuitManager.getInstance();
	}
	
	public void setCreation(Element elt){
		this.elt = elt;
		fireStartCreation();
	}
	
	public Element getCreation(){
		elt.setPosition(x, y);
		return elt.copy();
	}
	
	public int getX(){
		return x;
	}
	
	public void setX(int x){
		this.x=x;
		this.elt.setX(x);
		fireCoordinatesChanged();
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int y){
		this.y=y;
		this.elt.setY(y);
		fireCoordinatesChanged();
	}
	
	public void setCoordinates(int x, int y){
		this.x = x;
		this.y = y;
		this.elt.setPosition(x, y);
		fireCoordinatesChanged();
	}
	
	public void cancel(){
		this.elt = null;
		fireCancelled();
	}
	
	public void finish(){
		this.elt = null;
		fireFinishedCreation();
	}
	
	public boolean canCreate(){
		ElementDictionary dic = cm.getElementManager().getElementDictionary();
		elt.setPosition(x, y);
		return !dic.overlapping(elt);
	}
	
	public void addListener(ElementCreationManagerListener lst){
		if (!listeners.contains(lst)){
			listeners.add(lst);
		}
	}
	
	public void fireStartCreation(){
		for (ElementCreationManagerListener lst:listeners){
			lst.started();
		}
	}
	
	public void fireFinishedCreation(){
		for (ElementCreationManagerListener lst:listeners){
			lst.finished();
		}
	}
	
	public void fireCancelled(){
		for (ElementCreationManagerListener lst:listeners){
			lst.cancelled();
		}
	}
	
	public void fireCoordinatesChanged(){
		for (ElementCreationManagerListener lst:listeners){
			lst.changedCoordinates();
		}
	}
}
