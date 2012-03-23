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
public class SelectionManager {
	private ArrayList<SelectionManagerListener> listeners;
	private static SelectionManager sm;
	private static GroupManager gm;
	private Wire selectedWire;
	private WireEdge selectedWireEdge;

	public static SelectionManager getInstance() {
		if (sm == null) {
			sm = new SelectionManager();
			sm.listeners = new ArrayList<SelectionManagerListener>();
		}
		if (gm == null) {
			gm = new GroupManager(sm);
		}
		return sm;
	}

	protected void select(Element elt) {
		if (elt != null) {
			selectedWire = null;
			selectedWireEdge = null;
			gm.xorToGroup(elt);
			fireSelectionChanged();
		}
	}

	protected void select(List<Element> elts) {
		if (elts != null) {
			selectedWire = null;
			selectedWireEdge = null;
			gm.addToGroup(elts);
			fireSelectionChanged();
		}
	}

	protected void select(Wire wire) {
		if (wire != null) {
			gm.clear();
			selectedWireEdge = null;
			selectedWire = wire;
			fireSelectionChanged();
		}
	}

	protected void select(WireEdge edge) {
		if (edge != null) {
			gm.clear();
			selectedWire = null;
			selectedWireEdge = edge;
			fireSelectionChanged();
		}
	}

	protected void deselect() {
		gm.clear();
		selectedWire = null;
		selectedWireEdge = null;
		fireSelectionChanged();
	}

	public GroupManager getGroupManager() {
		return gm;
	}

	public Wire getSelectedWire() {
		return selectedWire;
	}

	public WireEdge getSelectedWireEdge() {
		return selectedWireEdge;
	}

	public SelectionManagerState exportState() {
		SelectionManagerState state = new SelectionManagerState();
		if (selectedWireEdge != null) {
			state.setWireEdge(selectedWireEdge.getParent(),
					selectedWireEdge.getIndex());
		}
		state.setWire(selectedWire);
		state.setElements((ArrayList<Element>) gm.getElements().clone());
		assert state.validate();
		return state;
	}

	/**
	 * This method should not be used directly, since the exporter may not be
	 * the importer. Instead, call SelectionManagerState.retore().
	 * 
	 * @param state
	 */
	private void importState(SelectionManagerState state) {
		assert state.validate();
		deselect();
		if (!state.getElements().isEmpty()) {
			select(state.getElements());
		} else if (state.getWire() != null) {
			select(state.getWire());
		} else if (state.getWireEdge() != null) {
			// The saved egde's parent's routing edge's ith element, where i =  edge's index.
			select(state.getWireEdge().getParent().getRoutingEdges().get(state.getWireEdge().getIndex()));
		} else {
			deselect();
		}
//		this.fireSelectionChanged();
	}

	public boolean isEmpty() {
		return selectedWire == null && selectedWireEdge == null && gm.isEmpty();
	}

	public void addListener(SelectionManagerListener lst) {
		if (!listeners.contains(lst)) {
			listeners.add(lst);
		}
	}

	public void fireSelectionChanged() {
		for (SelectionManagerListener lst : listeners) {
			lst.update();
		}
	}
	
	public void fireCircuitStateChanged(){
		CircuitManager.getInstance().fireStateChangedEvent();
		CircuitManager.getInstance().getEdgeManager().needToRenewOverlap();
		this.fireSelectionChanged();
	}

	class SelectionManagerState {
		ArrayList<Element> elements;
		Wire wire;
		WireEdge wireEdge;

		public ArrayList<Element> getElements() {
			return elements;
		}

		public void setElements(ArrayList<Element> elts) {
			this.elements = elts;
		}

		public Wire getWire() {
			return wire;
		}

		public void setWire(Wire wire) {
			this.wire = wire;
		}

		public WireEdge getWireEdge() {
			return wireEdge;
		}

		public void setWireEdge(Wire wire, int index) {
			this.wireEdge = wire.getRoutingEdges().get(index);
		}

		public boolean validate() {
			return (elements.isEmpty() && wire == null)
					|| (elements.isEmpty() && wireEdge == null)
					|| (wire == null && wireEdge == null);
		}

		/**
		 * This is the preferred way of restoring the state.
		 */
		public void restore() {
			SelectionManager.this.importState(this);
		}
	}
}
