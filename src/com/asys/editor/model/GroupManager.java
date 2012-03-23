/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.asys.editor.model.ElementManager.ElementDictionary;
import com.asys.model.components.exceptions.ElementOverlappingException;

/**
 * @author ryan
 * 
 */
public class GroupManager {
	private CircuitManager cm;
	private ElementManager em;
	private SelectionManager parent;
	private ArrayList<Element> elts;
	private boolean inducedWiresChanged, incidentWiresChanged;
	private ArrayList<Wire> ind_wires;
	private IncidentWireManager iwm;
	ElementDictionary dic, dic_exc;

	protected GroupManager(SelectionManager parent) {
		this.parent = parent;
		this.cm = CircuitManager.getInstance();
		this.em = cm.getElementManager();
		elts = new ArrayList<Element>();
		iwm = new IncidentWireManager();
		dic = em.getElementDictionary();
		dic_exc = em.getExcludeElementDictionary();
		setSelectionChanged();
	}
	
	public int getElementSize(){
		return elts.size();
	}

	public ArrayList<Element> getElements() {
		return elts;
	}

	protected void xorToGroup(Element elt_new) {
		if (elts.contains(elt_new)) {
			elts.remove(elt_new);
			dic_exc.include(elt_new);
		} else {
			elts.add(elt_new);
			dic_exc.exclude(elt_new);
		}
		setSelectionChanged();
	}

	protected void xorToGroup(List<Element> elts_new) {
		for (Element elt : elts_new) {
			this.xorToGroup(elt);
		}
		setSelectionChanged();
	}

	protected void addToGroup(List<Element> elts_new) {
		for (Element elt : elts_new) {
			if (!elts.contains(elt)) {
				elts.add(elt);
				dic_exc.exclude(elt);
			}
		}
		setSelectionChanged();
	}

	protected void removeFromGroup(Element elt) {
		elts.remove(elt);
		dic_exc.include(elt);
		setSelectionChanged();
	}

	protected void clear() {
		elts.clear();
		dic_exc.includeAll(); // Include all Elements when building 'dic_exc'
		setSelectionChanged();
	}

	private void setSelectionChanged() {
		this.inducedWiresChanged = true;
		this.incidentWiresChanged = true;
	}

	protected void copyToClipBoard() {
		ClipBoard cb = ClipBoard.getInstance();
		cb.clear();
		
		CircuitContainer cc = CircuitUtilities.copyCircuit(elts);
		ArrayList<Element> elts_cp = cc.getElements();
		ArrayList<Wire> wires_cp = cc.getWires();

		cb.setElements(elts_cp);
		cb.setWires(wires_cp);
	}
	
	public ArrayList<Wire> getInducedWires(){
		if (inducedWiresChanged){
			ind_wires = CircuitUtilities.getInducedWires(elts);
			inducedWiresChanged = false;
		}
		return ind_wires;
	}

	public IncidentWireManager getIncidentWireManager() {
		if (incidentWiresChanged) {
			ArrayList<Wire> all_in_wires = new ArrayList<Wire>();
			ArrayList<Wire> all_out_wires = new ArrayList<Wire>();
			for (Element elt : elts) {
				for (Inport ip : elt.getInports()) {
					if (ip != null && ip.getWire() != null) {
						all_in_wires.add(ip.getWire());
					}
				}
				for (Outport op : elt.getOutports()) {
					if (op != null && op.getWire() != null) {
						all_out_wires.add(op.getWire());
					}
				}
			}
			all_in_wires.removeAll(ind_wires);
			all_out_wires.removeAll(ind_wires);
			iwm.setInComingWires(all_in_wires);
			iwm.setOutGoingWires(all_out_wires);
			incidentWiresChanged = false;
		}
		return iwm;
	}

	protected void move(int dx, int dy) throws ElementOverlappingException {
		for (Element elt : elts) {
			int x = elt.getX() + dx;
			int y = elt.getY() + dy;
			boolean isOverlapping = dic_exc.overlapping(x, y, elt.getWidth(),
					elt.getHeight());
			if (isOverlapping) {
				throw new ElementOverlappingException();
			}
		}
		for (Element elt : elts) {
			elt.setPosition(elt.getX() + dx, elt.getY() + dy);
		}
		for (Wire wire : getInducedWires()) {
			wire.move(dx, dy);
		}
		for (Wire wire : getIncidentWireManager().getInComingWires()) {
			wire.adjustForInport();
		}
		for (Wire wire : getIncidentWireManager().getOutGoingWires()) {
			wire.adjustForOutport();
		}
		dic.moveAll(elts);
		dic_exc.moveAll(elts);
		parent.fireSelectionChanged();
		parent.fireCircuitStateChanged();
	}

	public boolean isEmpty() {
		return elts.isEmpty();
	}

	public boolean isInBound(Point p) {
		LinkedList<Element> list = dic.getElementsAtColumn(p.getX());
		if (list != null) {
			for (Element elt : list) {
				int y = elt.getY();
				int h = elt.getHeight();
				if (h <= 0) {
					if (y <= p.getY()
							&& p.getY() <= y+h) {
						if (elts.contains(elt)) {
							return true;
						}
					}
				} else {
					if (y < p.getY()
							&& p.getY() < y+h) {
						if (elts.contains(elt)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	class IncidentWireManager {
		private ArrayList<Wire> inComingWires, outGoingWires, allWires;
		private boolean hasAllWiresChanged;

		protected IncidentWireManager() {
			allWires = new ArrayList<Wire>();
			hasAllWiresChanged = true;
		}

		public ArrayList<Wire> getInComingWires() {
			return (ArrayList<Wire>) inComingWires.clone();
		}

		public void setInComingWires(ArrayList<Wire> inComingWires) {
			this.inComingWires = (ArrayList<Wire>) inComingWires.clone();
			hasAllWiresChanged = true;
		}

		public ArrayList<Wire> getOutGoingWires() {
			return (ArrayList<Wire>) outGoingWires.clone();
		}

		public void setOutGoingWires(ArrayList<Wire> outGoingWires) {
			this.outGoingWires = (ArrayList<Wire>) outGoingWires.clone();
			hasAllWiresChanged = true;
		}

		public ArrayList<Wire> getAllWires() {
			if (hasAllWiresChanged) {
				allWires.clear();
				allWires.addAll(getInComingWires());
				allWires.addAll(getOutGoingWires());
				hasAllWiresChanged = false;
			}
			return (ArrayList<Wire>) allWires.clone();
		}

	}
}
