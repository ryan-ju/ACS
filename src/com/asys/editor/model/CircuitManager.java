/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;

/**
 * 
 * This class is both a container and a mediator of all managers.
 * 
 */
public class CircuitManager {
	private static CircuitManager cm;
	private ArrayList<CircuitManagerListener> cmls;
	private static ElementManager eltm;
	private static WireManager wm;
	private static PortManager pm;
	private static EdgeManager edgm;

	public static CircuitManager getInstance(){
		if (cm == null){
			cm = new CircuitManager();
			wm = new WireManager();
			eltm = new ElementManager();
			edgm = new EdgeManager();
			wm.init();
			eltm.init();
			edgm.init();
			cm.cmls = new ArrayList<CircuitManagerListener>();
		}
		return cm;
	}
	
	protected void setElementManager(ElementManager eltm) {
		this.eltm = eltm;
	}

	protected void setWireManager(WireManager wm) {
		this.wm = wm;
	}

	protected void setPortManager(PortManager pm) {
		this.pm = pm;
	}

	public ElementManager getElementManager() {
		assert eltm != null;
		return eltm;
	}

	public WireManager getWireManager() {
		assert wm!=null;
		return wm;
	}

	public PortManager getPortManager() {
		return pm;
	}

	public EdgeManager getEdgeManager() {
		assert edgm != null;
		return edgm;
	}
	
	protected void setElementManagerChanged(){
		fireStateChangedEvent();
	}
	
	protected void setWireManagerChanged(){
		fireStateChangedEvent();
	}
	
	protected void setEdgeManagerChanged(){
		fireStateChangedEvent();
	}

	protected void fireStateChangedEvent() {
		for (CircuitManagerListener cml : cmls) {
			cml.update(this);
		}
	}
}
