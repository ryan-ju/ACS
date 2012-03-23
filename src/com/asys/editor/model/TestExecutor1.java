/**
 * 
 */
package com.asys.editor.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;

import com.asys.constants.CommandName;
import com.asys.constants.Direction;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;
import com.asys.views.BasicViewer;

/**
 * @author ryan
 * 
 */
public class TestExecutor1 {
	private static CircuitManager cm;
	private static ElementManager eltm;
	private static WireManager wm;
	private static EdgeManager edgm;
	private static SelectionManager sm;
	private static GroupManager gm;

	private static AndGate g1, g2, g3, g4, g5, g6, g7, g8, g9, g10;
	private static InputGate i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12,
			i13, i14;
	private static Wire w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13,
			w14, w15, w16, w17, w18, w19, w20, w21, w22, w23, w24;
	private static Executor ext;
	private static ElementCreationManager ecm;

	/**
	 * @throws java.lang.Exception
	 */
	public static void setUpBeforeClass() throws Exception {
		ext = Executor.getInstance();
		ecm = ElementCreationManager.getInstance();
		cm = CircuitManager.getInstance();
		eltm = cm.getElementManager();
		edgm = cm.getEdgeManager();
		wm = cm.getWireManager();
		sm = SelectionManager.getInstance();
		gm = sm.getGroupManager();
		i1 = new InputGate();
		i1.setPosition(4, 2);
		i2 = new InputGate();
		i2.setPosition(0, 4);
		i3 = new InputGate();
		i3.setPosition(4, 8);
		i4 = new InputGate();
		i4.setPosition(0, 10);
		i5 = new InputGate();
		i5.setPosition(8, 14);
		i6 = new InputGate();
		i6.setPosition(0, 14);
		i7 = new InputGate();
		i7.setPosition(8, 21);
		i8 = new InputGate();
		i8.setPosition(0, 23);
		i9 = new InputGate();
		i9.setPosition(35, 4);
		i10 = new InputGate();
		i10.setPosition(30, 6);
		i11 = new InputGate();
		i11.setPosition(35, 9);
		i12 = new InputGate();
		i12.setPosition(30, 11);
		i13 = new InputGate();
		i13.setPosition(35, 14);
		i14 = new InputGate();
		i14.setPosition(30, 16);

		g1 = new AndGate();
		g1.setPosition(8, 3);
		g2 = new AndGate();
		g2.setPosition(8, 9);
		g3 = new AndGate();
		g3.setPosition(15, 13);
		g4 = new AndGate();
		g4.setPosition(15, 22);
		g5 = new AndGate();
		g5.setPosition(17, 6);
		g6 = new AndGate();
		g6.setPosition(40, 5);
		g7 = new AndGate();
		g7.setPosition(40, 10);
		g8 = new AndGate();
		g8.setPosition(40, 15);
		g9 = new AndGate();
		g9.setPosition(40, 19);
		g9.setNumberOfIPs(3);
		g10 = new AndGate();
		g10.setPosition(60, 10);
		g10.setNumberOfIPs(5);
		Method setMaxIPs = Element.class.getDeclaredMethod("setMaxIPs",
				new Class[] { int.class });
		setMaxIPs.setAccessible(true);
		setMaxIPs.invoke(g9, new Object[] { new Integer(3) });
		System.out.println("g9.getMaxIPs()==" + g9.getNumberOfIPs());
		setMaxIPs.invoke(g10, new Object[] { new Integer(5) });
		LinkedList<RoutingPoint> w1_rps = new LinkedList<RoutingPoint>();
		w1 = new Wire(i1.getOutport(0), g1.getInport(0), w1_rps);
		LinkedList<RoutingPoint> w2_rps = new LinkedList<RoutingPoint>();
		w2 = new Wire(i2.getOutport(0), g1.getInport(1), w2_rps);
		LinkedList<RoutingPoint> w3_rps = new LinkedList<RoutingPoint>();
		w3 = new Wire(i3.getOutport(0), g2.getInport(0), w3_rps);
		LinkedList<RoutingPoint> w4_rps = new LinkedList<RoutingPoint>();
		w4 = new Wire(i4.getOutport(0), g2.getInport(1), w4_rps);
		LinkedList<RoutingPoint> w5_rps = new LinkedList<RoutingPoint>();
		w5_rps.add(new RoutingPoint(14, 16));
		w5_rps.add(new RoutingPoint(14, 14));
		w5 = new Wire(i5.getOutport(0), g3.getInport(0), w5_rps);
		LinkedList<RoutingPoint> w6_rps = new LinkedList<RoutingPoint>();
		w6 = new Wire(i6.getOutport(0), g3.getInport(1), w6_rps);
		LinkedList<RoutingPoint> w7_rps = new LinkedList<RoutingPoint>();
		w7 = new Wire(i7.getOutport(0), g4.getInport(0), w7_rps);
		LinkedList<RoutingPoint> w8_rps = new LinkedList<RoutingPoint>();
		w8 = new Wire(i8.getOutport(0), g4.getInport(1), w8_rps);
		LinkedList<RoutingPoint> w9_rps = new LinkedList<RoutingPoint>();
		w9_rps.add(new RoutingPoint(15, 5));
		w9_rps.add(new RoutingPoint(15, 7));
		w9 = new Wire(g1.getOutport(0), g5.getInport(0), w9_rps);
		LinkedList<RoutingPoint> w10_rps = new LinkedList<RoutingPoint>();
		w10_rps.add(new RoutingPoint(15, 11));
		w10_rps.add(new RoutingPoint(15, 9));
		w10 = new Wire(g2.getOutport(0), g5.getInport(1), w10_rps);
		LinkedList<RoutingPoint> w11_rps = new LinkedList<RoutingPoint>();
		w11_rps.add(new RoutingPoint(22, 15));
		w11_rps.add(new RoutingPoint(22, 21));
		w11 = new Wire(g3.getOutport(0), g9.getInport(1), w11_rps);
		LinkedList<RoutingPoint> w12_rps = new LinkedList<RoutingPoint>();
		w12_rps.add(new RoutingPoint(22, 24));
		w12_rps.add(new RoutingPoint(22, 22));
		w12 = new Wire(g4.getOutport(0), g9.getInport(2), w12_rps);
		LinkedList<RoutingPoint> w13_rps = new LinkedList<RoutingPoint>();
		w13_rps.add(new RoutingPoint(24, 8));
		w13_rps.add(new RoutingPoint(24, 20));
		w13 = new Wire(g5.getOutport(0), g9.getInport(0), w13_rps);
		LinkedList<RoutingPoint> w14_rps = new LinkedList<RoutingPoint>();
		w14 = new Wire(i9.getOutport(0), g6.getInport(0), w14_rps);
		LinkedList<RoutingPoint> w15_rps = new LinkedList<RoutingPoint>();
		w15 = new Wire(i10.getOutport(0), g6.getInport(1), w15_rps);
		LinkedList<RoutingPoint> w16_rps = new LinkedList<RoutingPoint>();
		w16 = new Wire(i11.getOutport(0), g7.getInport(0), w16_rps);
		LinkedList<RoutingPoint> w17_rps = new LinkedList<RoutingPoint>();
		w17 = new Wire(i12.getOutport(0), g7.getInport(1), w17_rps);
		LinkedList<RoutingPoint> w18_rps = new LinkedList<RoutingPoint>();
		w18 = new Wire(i13.getOutport(0), g8.getInport(0), w18_rps);
		LinkedList<RoutingPoint> w19_rps = new LinkedList<RoutingPoint>();
		w19 = new Wire(i14.getOutport(0), g8.getInport(1), w19_rps);
		LinkedList<RoutingPoint> w20_rps = new LinkedList<RoutingPoint>();
		w20 = new Wire(g6.getOutport(0), g10.getInport(0), w20_rps);
		w20_rps.add(new RoutingPoint(55, 7));
		w20_rps.add(new RoutingPoint(55, 11));
		LinkedList<RoutingPoint> w21_rps = new LinkedList<RoutingPoint>();
		w21 = new Wire(g7.getOutport(0), g10.getInport(1), w21_rps);
		LinkedList<RoutingPoint> w22_rps = new LinkedList<RoutingPoint>();
		w22_rps.add(new RoutingPoint(50, 17));
		w22_rps.add(new RoutingPoint(50, 13));
		w22 = new Wire(g8.getOutport(0), g10.getInport(2), w22_rps);
		LinkedList<RoutingPoint> w23_rps = new LinkedList<RoutingPoint>();
		w23 = new Wire(g9.getOutport(0), g10.getInport(3), w23_rps);
		w23_rps.add(new RoutingPoint(52, 21));
		w23_rps.add(new RoutingPoint(52, 14));
		LinkedList<RoutingPoint> w24_rps = new LinkedList<RoutingPoint>();
		w24_rps.add(new RoutingPoint(70, 13));
		w24_rps.add(new RoutingPoint(70, 18));
		w24_rps.add(new RoutingPoint(54, 18));
		w24_rps.add(new RoutingPoint(54, 15));
		w24 = new Wire(g10.getOutport(0), g10.getInport(4), w24_rps);
		g10.setOrientation(Direction.LEFT);
		w20.adjustForInport();
		w21.adjustForInport();
		w22.adjustForInport();
		w23.adjustForInport();
		w24.adjustForInport();
		w24.adjustForOutport();
	}

	public static void main(String[] args) {
		try {
			setUpBeforeClass();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<Element> elts = new ArrayList<Element>();

		elts.add(g9);

		ArrayList<Element> all_elts = new ArrayList<Element>();
		all_elts.add(g1);
		all_elts.add(g2);
		all_elts.add(g3);
		all_elts.add(g4);
		all_elts.add(g5);
		all_elts.add(g6);
		all_elts.add(g7);
		all_elts.add(g8);
		all_elts.add(g9);
		all_elts.add(g10);
		all_elts.add(i1);
		all_elts.add(i1);
		all_elts.add(i1);
		all_elts.add(i2);
		all_elts.add(i3);
		all_elts.add(i4);
		all_elts.add(i5);
		all_elts.add(i6);
		all_elts.add(i7);
		all_elts.add(i8);
		all_elts.add(i9);
		all_elts.add(i10);
		all_elts.add(i11);
		all_elts.add(i12);
		all_elts.add(i13);
		all_elts.add(i14);
		ArrayList<Wire> wires = new ArrayList<Wire>();
		wires.add(w1);
		wires.add(w2);
		wires.add(w3);
		wires.add(w4);
		wires.add(w5);
		wires.add(w6);
		wires.add(w7);
		wires.add(w8);
		wires.add(w9);
		wires.add(w10);
		wires.add(w11);
		wires.add(w12);
		wires.add(w13);
		wires.add(w14);
		wires.add(w15);
		wires.add(w16);
		wires.add(w17);
		wires.add(w18);
		wires.add(w19);
		wires.add(w20);
		wires.add(w21);
		wires.add(w22);
		wires.add(w23);
		wires.add(w24);

		eltm.addElement(all_elts);
		wm.addWire(wires);

		// Setup 'ecm'
		ecm.setCreation(new AndGate());
		// Create a Command instance
		Command cmd = Command.getInstance();
		// Create an AndGate
		cmd.setCommandName(CommandName.CREATE_GATE);
		cmd.setParams(new Object[] { 30, 30 });
		// 'ext' excutes 'cmd'
		ext.execute(cmd);

		cmd.setCommandName(CommandName.SELECT_ELEMENT);
		Element selected_element = eltm.getElementAt(31, 31);
		System.out.println("selected_element == null? "
				+ (selected_element == null));
		System.out.println("selected_element.getX() = "
				+ selected_element.getX());
		System.out.println("selected_element.getY() = "
				+ selected_element.getY());
		cmd.setParams(new Object[] { selected_element }); // Get the AndGate
															// just created.
		ext.execute(cmd);
		ArrayList<Element> selected_elements = gm.getElements();
		System.out.println("selected_element.size() = "
				+ selected_elements.size() + ", should be 1");
		Element element_in_gm = selected_elements.get(0);
		// Create a Wire
		cmd.setCommandName(CommandName.CREATE_WIRE);
		LinkedList<RoutingPoint> rps = new LinkedList<RoutingPoint>();
		rps.add(new RoutingPoint(40, 32));
		rps.add(new RoutingPoint(40, 38));
		rps.add(new RoutingPoint(25, 38));
		rps.add(new RoutingPoint(25, 33));
		try {
			cmd.setParams(new Object[] { element_in_gm.getOutport(0),
					element_in_gm.getInport(1), rps });
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ext.execute(cmd);

		// Select a wire
		Wire selected_wire = wm.getWireAt(50, 15);
		assert selected_wire != null;
		System.out.println("selected_wire.getOutport() position is ("
				+ selected_wire.getOutport().getPosition().getX() + ", "
				+ selected_wire.getOutport().getPosition().getY() + ")");
		cmd.setCommandName(CommandName.SELECT_WIRE);
		cmd.setParams(new Object[] { selected_wire });
		ext.execute(cmd);

		// Select a wire
		selected_wire = wm.getWireAt(16, 7);
		assert selected_wire != null;
		System.out.println("selected_wire.getOutport() position is ("
				+ selected_wire.getOutport().getPosition().getX() + ", "
				+ selected_wire.getOutport().getPosition().getY() + ")");
		cmd.setCommandName(CommandName.SELECT_WIRE);
		cmd.setParams(new Object[] { selected_wire });
		ext.execute(cmd);

		// Select a wire edge
		WireEdge selected_wire_edge = edgm.getEdgeAt(25, 21);
		assert selected_wire_edge != null;
		System.out.println("selected_wire_edge's end points are ("
				+ selected_wire_edge.getP1().getX() + ", "
				+ selected_wire_edge.getP1().getY() + ") and ("
				+ selected_wire_edge.getP2().getX() + ", "
				+ selected_wire_edge.getP2().getY() + ")");
		cmd.setCommandName(CommandName.SELECT_WIRE_EDGE);
		cmd.setParams(new Object[] { selected_wire_edge });
		ext.execute(cmd);

		// Move the wire edge
		// cmd.setCommandName(CommandName.MOVE);
		// cmd.setParams(new Object[]{10, 10});
		// ext.execute(cmd);
		//
		// cmd.setCommandName(CommandName.UNDO);
		// ext.execute(cmd);

		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);
		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);
		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);
		cmd.setCommandName(CommandName.REDO);
		ext.execute(cmd);
		ext.execute(cmd);
		ext.execute(cmd);

		// Add a wire edge
		Wire wire_target = selected_wire_edge.getParent();
		int index = 2;
		cmd.setCommandName(CommandName.CREATE_WIRE_EDGE);
		cmd.setParams(new Object[] { wire_target, index, 30, 11, 39, 39 });
		ext.execute(cmd);

		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);

		// Select a wire
		selected_wire = wm.getWireAt(16, 7);
		assert selected_wire != null;
		System.out.println("selected_wire.getOutport() position is ("
				+ selected_wire.getOutport().getPosition().getX() + ", "
				+ selected_wire.getOutport().getPosition().getY() + ")");
		cmd.setCommandName(CommandName.SELECT_WIRE);
		cmd.setParams(new Object[] { selected_wire });
		ext.execute(cmd);

		// Delete the selected wire
		cmd.setCommandName(CommandName.DELETE);
		ext.execute(cmd);

		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);

		// //Deselect
		// cmd.setCommandName(CommandName.DESELECT);
		// ext.execute(cmd);
		//
		// //Select an Element
		selected_element = eltm.getElementAt(31, 31);
		cmd.setCommandName(CommandName.SELECT_ELEMENT);
		cmd.setParams(new Object[] { selected_element });
		ext.execute(cmd);
		//
		// // Move the Element
		cmd.setCommandName(CommandName.MOVE);
		cmd.setParams(new Object[] { 15, 10 });
		ext.execute(cmd);

		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);

		// Create a Fanout
		ecm.setCreation(new Fanout());
		cmd.setCommandName(CommandName.CREATE_FANOUT);
		try {
			cmd.setParams(new Object[] {
					selected_element.getOutport(0).getWire(), 2, 40, 38 });
		} catch (PortNumberOutOfBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ext.execute(cmd);

		// cmd.setCommandName(CommandName.UNDO);
		// ext.execute(cmd);

		Rectangle select_region = new Rectangle();
		select_region.setPosition(0, 0);
		select_region.setHeight(30);
		select_region.setWidth(30);
		selected_element = eltm.getElementsInRectangle(select_region).get(0);
		sm.select(eltm.getElementsInRectangle(select_region));

		// Move selected
		cmd.setCommandName(CommandName.MOVE);
		cmd.setParams(new Object[] { 0, 5 });
		ext.execute(cmd);

		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);

		// Delete the Element
		// cmd.setCommandName(CommandName.DELETE);
		// ext.execute(cmd);
		//
		// cmd.setCommandName(CommandName.UNDO);
		// ext.execute(cmd);

		// Select a wire
		selected_wire = wm.getWireAt(40, 35);
		assert selected_wire != null;
		System.out.println("selected_wire.getOutport() position is ("
				+ selected_wire.getOutport().getPosition().getX() + ", "
				+ selected_wire.getOutport().getPosition().getY() + ")");
		cmd.setCommandName(CommandName.SELECT_WIRE);
		cmd.setParams(new Object[] { selected_wire });
		ext.execute(cmd);

		// Add Fanout
		cmd.setCommandName(CommandName.CREATE_FANOUT);
		cmd.setParams(new Object[] { selected_wire, 1, 40, 35 });
		ext.execute(cmd);

		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);
		// cmd.setCommandName(CommandName.REDO);
		// ext.execute(cmd);

		// Select a WireEdge
		selected_wire = wm.getWireAt(25, 38);
		cmd.setCommandName(CommandName.SELECT_WIRE_EDGE);
		cmd.setParams(new Object[] { selected_wire.getRoutingEdges().get(1) });
		ext.execute(cmd);

		// Move the selected WireEdge
		cmd.setCommandName(CommandName.MOVE);
		cmd.setParams(new Object[] { 10, -10 });
		ext.execute(cmd);

		cmd.setCommandName(CommandName.UNDO);
		ext.execute(cmd);
		//

		//
		// gm.xorToGroup(elts);
		// // Copy group 4
		// gm.copyToClipBoard();
		// ClipBoard cb = ClipBoard.getInstance();
		// try {
		// gm.move(10, 10);
		// } catch (ElementOverlappingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// gm.move(-21, -20);
		// } catch (ElementOverlappingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// BasicViewer bv_gm = new BasicViewer(gm);
		// bv_gm.setVisible(true);
		// BasicViewer bv_cb = new BasicViewer(cb);
		// bv_cb.setVisible(true);
		BasicViewer bv_cm = new BasicViewer(cm);
		bv_cm.setVisible(true);

	}
}
