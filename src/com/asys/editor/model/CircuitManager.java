/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;

/**
 * @author ryan
 * 
 */
public class CircuitManager {
	private static CircuitManager cm;
	private ArrayList<CircuitManagerListener> cmls;
	private static ElementManager eltm;
	private static WireManager wm;
	private static PortManager pm;

	public static CircuitManager getInstance(){
		if (cm == null){
			cm = new CircuitManager();
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
		if (eltm == null)
			eltm = new ElementManager(this);
		return eltm;
	}

	public WireManager getWireManager() {
		if (wm == null){
			wm = new WireManager(this);
		}
		return wm;
	}

	public PortManager getPortManager() {
		if (pm == null)
			pm = new PortManager(this);
		return pm;
	}

	public EdgeManager getEdgeManager() {
		return wm.getEdgeManager();
	}

	protected void fireStateChangedEvent() {
		for (CircuitManagerListener cml : cmls) {
			cml.update(this);
		}
	}
}
