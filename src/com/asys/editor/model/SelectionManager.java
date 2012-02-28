/**
 * 
 */
package com.asys.editor.model;

/**
 * @author ryan
 * 
 */
public class SelectionManager {
	private static GroupManager gm;
	private static SelectionManager sm;
	private CircuitManager cm = CircuitManager.getInstance();
	private ElementManager em = cm.getElementManager();
	private WireManager wm = cm.getWireManager();

	public SelectionManager getInstance() {
		if (sm == null) {
			sm = new SelectionManager();
		}
		if (gm == null) {
			gm = new GroupManager(sm);
		}
		return sm;
	}

//	public Action select(int x, int y) {
//
//	}
//
//	public Action selectElement(int x, int y, int w, int h) {
//
//	}
//
//	private Action selectElement(int x, int y) {
//
//	}
//
//	private Action selectWire(int x, int y) {
//
//	}
//
//	private Aciton selectEdge(int x, int y) {
//
//	}
}
