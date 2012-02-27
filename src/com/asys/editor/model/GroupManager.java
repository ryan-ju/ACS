/**
 * 
 */
package com.asys.editor.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ryan
 * 
 */
public class GroupManager {
	private SelectionManager parent;
	private ArrayList<Element> elts;

	public GroupManager(SelectionManager parent) {
		this.parent = parent;
		elts = new ArrayList<Element>();
	}

	public List<Element> getElements() {
		return elts;
	}

	public void setSelection(ArrayList<Element> elts) {
		this.elts = elts;
	}

	public void deselect(Element elt) {
		elts.remove(elt);
	}

	public void deselectAll() {
		elts.clear();
	}

	public void copyToClipBoard(){
		ClipBoard cb = ClipBoard.getInstance();
		cb.clear();
		
		ArrayList<Wire> wires_cp = new ArrayList<Wire>();
		ArrayList<Element> elts_cp = new ArrayList<Element>();
		
		// The following code generates a mapping between the original and the copied Elements, Wires, Inports and Outports.
		HashMap<Inport, Inport> ip_map = new HashMap<Inport, Inport>();
		HashMap<Outport, Outport> op_map = new HashMap<Outport, Outport>();
		HashMap<Element, Element> elt_map = new HashMap<Element, Element>();
		HashMap<Wire, Wire> wire_map = new HashMap<Wire, Wire>();
		ArrayList<Wire> inducedWires = getInducedWires();
		for (Wire wire:inducedWires){
			Wire wire_cp = wire.copy();
			wire_map.put(wire, wire_cp);
			wires_cp.add(wire_cp);
		}
		for (Element elt:elts){
			Element elt_cp = elt.copy();
			elt_map.put(elt, elt_cp);
			elts_cp.add(elt_cp);
			for (int i = 0; i< elt.getInports().size();i++){
				ip_map.put(elt.getInports().get(i), elt_cp.getInports().get(i));
			}
			for (int i=0; i<elt.getOutports().size();i++){
				op_map.put(elt.getOutports().get(i), elt_cp.getOutports().get(i));
			}
		}
		
		for (Wire wire:inducedWires){
			Wire wire_cp = wire_map.get(wire);
			Inport ip_cp = ip_map.get(wire.getInport());
			Outport op_cp = op_map.get(wire.getOutport());
			wire_cp.setInport(ip_cp);
			wire_cp.setOutport(op_cp);
		}
		
		cb.setElements(elts_cp);
		cb.setWires(wires_cp);
	}

	public ArrayList<Wire> getInducedWires() {
		ArrayList<Wire> ind_wires = new ArrayList<Wire>();
		ArrayList<Element> notSeen = (ArrayList<Element>) elts.clone();
		ArrayList<Element> eltTemp = new ArrayList<Element>();
		while (!notSeen.isEmpty()) {
			eltTemp.add(notSeen.get(0));
			/*
			 * Inv: x is seen implies any x's neighbour y in the manager is seen
			 * or in eltTemp, and wire x-y has been added to the copy. && elt is
			 * seen and is not in eltTemp anymore.
			 */
			while (!eltTemp.isEmpty()) {
				Element elt = eltTemp.remove(0);
				for (Object[] n : neighbours(elt)) {
					if (notSeen.contains((Element) n[3])) {
						eltTemp.add((Element) n[3]);
						ind_wires.add((Wire) n[1]);
					}
				}
				notSeen.remove(elt);
			}
		}
		return ind_wires;
	}

	/**
	 * Neighours are confined within this group.
	 * 
	 * @param elt
	 * @return ArrayList<Object[]>. Each element in the array list represents a
	 *         neighbour, which is represented as an Object[] instance of type
	 *         {Inport(of the wire), Wire, Outport(of the wire), Element(the
	 *         neighbour)}.
	 */
	private List<Object[]> neighbours(Element elt) {
		LinkedList<Element> ns = new LinkedList<Element>();
		LinkedList<Object[]> nss = new LinkedList<Object[]>();
		List<Inport> ips = elt.getInports();
		for (Inport ip : ips) {
			Wire wire = ip.getWire();
			if (wire != null) { // Make sure the port has a wire
								// connected to.
//				assert wire.getOutport() != null;
				if (wire.getOutport() == null){
					System.out.println("Error");
				}
				Outport op_nb = wire.getOutport();
				Element nb = op_nb.getParent();
				if (!ns.contains(nb)) {
					ns.add(nb);
					nss.add(new Object[] { wire.getInport(), wire, op_nb, nb });
				}
			}
		}
		List<Outport> ops = elt.getOutports();
		for (Outport op : ops) {
			Wire wire = op.getWire();
			if (op.getWire() != null) {// Make sure the port has a wire
										// connected to.
				assert wire.getOutport() != null;
				Inport ip_nb = wire.getInport();
				Element nb = ip_nb.getParent();
				if (!ns.contains(nb)) {
					ns.add(nb);
					nss.add(new Object[] { ip_nb, wire, wire.getOutport(), nb });
				}
			}
		}
		return nss;
	}
}