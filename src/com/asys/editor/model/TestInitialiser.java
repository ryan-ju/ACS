/**
 * 
 */
package com.asys.editor.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;

import com.asys.constants.Direction;
import com.asys.constants.ElementPropertyKey;
import com.asys.model.components.exceptions.DuplicateElementException;
import com.asys.model.components.exceptions.InvalidPropertyException;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.NoKeyException;
import com.asys.model.components.exceptions.OverlappingElementException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;

/**
 * @author ryan
 *
 */
public class TestInitialiser {
	public static void initialise() throws PortNumberOutOfBoundException, MaxNumberOfPortsOutOfBoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException{
		env_gate();
		
	}
	
	public static void mixed_gates() throws SecurityException, NoSuchMethodException, PortNumberOutOfBoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, MaxNumberOfPortsOutOfBoundException{
		CircuitManager cm = CircuitManager.getInstance();
		WireManager wm = cm.getWireManager();
		ElementManager em = cm.getElementManager();
		
		AndGate g1, g6, g7, g8, g9, g10;
		XorGate g5;
		NorGate g4, nor1;
		NandGate g3, nand1;
		OrGate g2;
		NotGate n1, n2;
		InputGate i1, i3, i6, i8, i9, i10, i11, i12,
				i13, i14, i15;
		EnvironmentGate e2, e4, e5, e7;
		Fanout f1;
		OutputGate o1;
		Wire w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13,
				w14, w15, w16, w17, w18, w19, w20, w21, w22, w23, w24, w25, w26, w27, w28;
		
		i1 = new InputGate();
		i1.setPosition(4, 2);
		e2 = new EnvironmentGate();
		e2.setPosition(0, 4);
		i3 = new InputGate();
		i3.setPosition(4,8);
		e4 = new EnvironmentGate();
		e4.setPosition(0, 10);
		e5 = new EnvironmentGate();
		e5.setPosition(8, 14);
		i6 = new InputGate();
		i6.setPosition(0, 14);
		e7 = new EnvironmentGate();
		e7.setPosition(8, 21);
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
		i15 = new InputGate();
		i15.setPosition(20, 32);
		i15.setOrientation(Direction.UP);
		
		o1 = new OutputGate();
		o1.setPosition(60, 19);
		
		g1 = new AndGate();
		g1.setPosition(8, 3);
		g2 = new OrGate();
		g2.setPosition(8, 9);
		g3 = new NandGate();
		g3.setPosition(15, 13);
		g4 = new NorGate();
		g4.setPosition(15, 22);
		g5 = new XorGate();
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
		nand1 = new NandGate();
		nand1.setPosition(30, 30);
		nor1 = new NorGate();
		nor1.setPosition(40, 30);
		
		n1 = new NotGate();
		n1.setPosition(50, 5);
		n2 = new NotGate();
		n2.setPosition(22, 10);
		n2.setOrientation(Direction.DOWN);
		
		f1 = new Fanout();
		f1.setPosition(52, 21);
		
//		Method setMaxIPs = Element.class.getDeclaredMethod("setMaxIPs",
//				new Class[] { int.class });
//		setMaxIPs.setAccessible(true);
//		setMaxIPs.invoke(g9, new Object[] { new Integer(3) });
//		System.out.println("g9.getMaxIPs()==" + g9.getNumberOfIPs());
//		setMaxIPs.invoke(g10, new Object[] { new Integer(5) });
		
		LinkedList<RoutingPoint> w1_rps = new LinkedList<RoutingPoint>();
		w1 = new Wire(i1.getOutport(0), g1.getInport(0), w1_rps);
		LinkedList<RoutingPoint> w2_rps = new LinkedList<RoutingPoint>();
		w2 = new Wire(e2.getOutport(0), g1.getInport(1), w2_rps);
		LinkedList<RoutingPoint> w3_rps = new LinkedList<RoutingPoint>();
		w3 = new Wire(i3.getOutport(0), g2.getInport(0), w3_rps);
		LinkedList<RoutingPoint> w4_rps = new LinkedList<RoutingPoint>();
		w4 = new Wire(e4.getOutport(0), g2.getInport(1), w4_rps);
		LinkedList<RoutingPoint> w5_rps = new LinkedList<RoutingPoint>();
		w5_rps.add(new RoutingPoint(14, 16));
		w5_rps.add(new RoutingPoint(14, 14));
		w5 = new Wire(e5.getOutport(0), g3.getInport(0), w5_rps);
		LinkedList<RoutingPoint> w6_rps = new LinkedList<RoutingPoint>();
		w6 = new Wire(i6.getOutport(0), g3.getInport(1), w6_rps);
		LinkedList<RoutingPoint> w7_rps = new LinkedList<RoutingPoint>();
		w7 = new Wire(e7.getOutport(0), g4.getInport(0), w7_rps);
		LinkedList<RoutingPoint> w8_rps = new LinkedList<RoutingPoint>();
		w8 = new Wire(i8.getOutport(0), g4.getInport(1), w8_rps);
		LinkedList<RoutingPoint> w9_rps = new LinkedList<RoutingPoint>();
		w9_rps.add(new RoutingPoint(15, 5));
		w9_rps.add(new RoutingPoint(15,7));
		w9 = new Wire(g1.getOutport(0), g5.getInport(0), w9_rps);
		LinkedList<RoutingPoint> w10_rps = new LinkedList<RoutingPoint>();
		w10_rps.add(new RoutingPoint(15,11));
		w10_rps.add(new RoutingPoint(15,9));
		w10 = new Wire(g2.getOutport(0), g5.getInport(1), w10_rps);
		LinkedList<RoutingPoint> w11_rps = new LinkedList<RoutingPoint>();
		w11_rps.add(new RoutingPoint(22,15));
		w11_rps.add(new RoutingPoint(22,21));
		w11 = new Wire(g3.getOutport(0), g9.getInport(1), w11_rps);
		LinkedList<RoutingPoint> w12_rps = new LinkedList<RoutingPoint>();
		w12_rps.add(new RoutingPoint(22,24));
		w12_rps.add(new RoutingPoint(22,22));
		w12 = new Wire(g4.getOutport(0), g9.getInport(2), w12_rps);
		LinkedList<RoutingPoint> w13_rps = new LinkedList<RoutingPoint>();
		w13_rps.add(new RoutingPoint(24,8));
		w13 = new Wire(g5.getOutport(0), n2.getInport(0), w13_rps);
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
		w20 = new Wire(g6.getOutport(0), n1.getInport(0), w20_rps);
		LinkedList<RoutingPoint> w21_rps = new LinkedList<RoutingPoint>();
		w21 = new Wire(g7.getOutport(0), g10.getInport(1), w21_rps);
		LinkedList<RoutingPoint> w22_rps = new LinkedList<RoutingPoint>();
		w22_rps.add(new RoutingPoint(50,17));
		w22_rps.add(new RoutingPoint(50,13));
		w22 = new Wire(g8.getOutport(0), g10.getInport(2), w22_rps);
		LinkedList<RoutingPoint> w23_rps = new LinkedList<RoutingPoint>();
		w23 = new Wire(g9.getOutport(0), f1.getInport(0), w23_rps);
		LinkedList<RoutingPoint> w24_rps = new LinkedList<RoutingPoint>();
		w24_rps.add(new RoutingPoint(70,13));
		w24_rps.add(new RoutingPoint(70,18));
		w24_rps.add(new RoutingPoint(54,18));
		w24_rps.add(new RoutingPoint(54,15));
		w24 = new Wire(g10.getOutport(0), g10.getInport(4), w24_rps);
		LinkedList<RoutingPoint> w25_rps = new LinkedList<RoutingPoint>();
		w25_rps.add(new RoutingPoint(24,20));
		w25 = new Wire(n2.getOutport(0), g9.getInport(0), w25_rps);
		LinkedList<RoutingPoint> w26_rps = new LinkedList<RoutingPoint>();
		w26_rps.add(new RoutingPoint(55,7));
		w26_rps.add(new RoutingPoint(55,11));
		w26 = new Wire(n1.getOutport(0), g10.getInport(0), w26_rps);
		LinkedList<RoutingPoint> w27_rps = new LinkedList<RoutingPoint>();
		w27_rps.add(new RoutingPoint(52,14));
		w27 = new Wire(f1.getOutport(0), g10.getInport(3), w27_rps);
		LinkedList<RoutingPoint> w28_rps = new LinkedList<RoutingPoint>();
		w28 = new Wire(f1.getOutport(1), o1.getInport(0), w28_rps);
		
		w20.adjustForInport();
		w21.adjustForInport();
		w22.adjustForInport();
		w23.adjustForInport();
		w24.adjustForInport();
		w24.adjustForOutport();
		
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
		all_elts.add(e2);
		all_elts.add(i3);
		all_elts.add(e4);
		all_elts.add(e5);
		all_elts.add(i6);
		all_elts.add(e7);
		all_elts.add(i8);
		all_elts.add(i9);
		all_elts.add(i10);
		all_elts.add(i11);
		all_elts.add(i12);
		all_elts.add(i13);
		all_elts.add(i14);
		all_elts.add(n1);
		all_elts.add(n2);
		all_elts.add(f1);
		all_elts.add(o1);
		all_elts.add(nand1);
		all_elts.add(nor1);
		all_elts.add(i15);
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
		wires.add(w25);
		wires.add(w26);
		wires.add(w27);
		wires.add(w28);
		
		em.addElement(all_elts);
		wm.addWire(wires);
	}
	
	public static void and_gates_only() throws PortNumberOutOfBoundException, MaxNumberOfPortsOutOfBoundException{
		CircuitManager cm = CircuitManager.getInstance();
		WireManager wm = cm.getWireManager();
		ElementManager em = cm.getElementManager();

		AndGate a1, a2, a3, a4;
		InputGate i1, i2, i3;
		Wire w1, w2, w3, w4, w5, w6, w7, w8;
		Fanout f1;

		a1 = new AndGate();
		a1.setPosition(6, 15);
		a2 = new AndGate();
		a2.setPosition(43, 13);
		a2.setOrientation(Direction.UP);
		a2.setNumberOfIPs(3);
		a3 = new AndGate();
		a3.setPosition(47, 28);
		a3.setOrientation(Direction.LEFT);
		a4 = new AndGate();
		a4.setPosition(55, 20);
		i1 = new InputGate();
		i1.setPosition(0, 14);
		i2 = new InputGate();
		i2.setPosition(15, 28);
		i3 = new InputGate();
		i3.setPosition(60, 28);
		i3.setOrientation(Direction.LEFT);
		f1 = new Fanout();
		f1.setPosition(56, 30);
		w1 = new Wire(i1.getOutport(0), a1.getInport(0),
				new LinkedList<RoutingPoint>());
		LinkedList<RoutingPoint> w2rps = new LinkedList<RoutingPoint>();
		w2rps.addLast(new RoutingPoint(23, 30));
		w2rps.addLast(new RoutingPoint(23, 25));
		w2rps.addLast(new RoutingPoint(3, 25));
		w2rps.addLast(new RoutingPoint(3, 18));
		w2 = new Wire(i2.getOutport(0), a1.getInport(1), w2rps);
		LinkedList<RoutingPoint> w3rps = new LinkedList<RoutingPoint>();
		w3rps.addLast(new RoutingPoint(33, 17));
		w3rps.addLast(new RoutingPoint(33, 25));
		w3rps.addLast(new RoutingPoint(44, 25));
		w3 = new Wire(a1.getOutport(0), a2.getInport(0), w3rps);
		LinkedList<RoutingPoint> w4rps = new LinkedList<RoutingPoint>();
		w4rps.addLast(new RoutingPoint(45, 3));
		w4rps.addLast(new RoutingPoint(50, 3));
		w4rps.addLast(new RoutingPoint(50, 26));
		w4rps.addLast(new RoutingPoint(46, 26));
		w4 = new Wire(a2.getOutport(0), a2.getInport(2), w4rps);
		LinkedList<RoutingPoint> w5rps = new LinkedList<RoutingPoint>();
		w5rps.addLast(new RoutingPoint(45, 30));
		w5 = new Wire(a3.getOutport(0), a2.getInport(1), w5rps);
		LinkedList<RoutingPoint> w6rps = new LinkedList<RoutingPoint>();
		w6rps.addLast(new RoutingPoint(56, 29));
		w6 = new Wire(f1.getOutport(0), a3.getInport(0), w6rps);
		LinkedList<RoutingPoint> w7rps = new LinkedList<RoutingPoint>();
		w7rps.addLast(new RoutingPoint(56, 31));
		w7 = new Wire(f1.getOutport(1), a3.getInport(1), w7rps);
		LinkedList<RoutingPoint> w8rps = new LinkedList<RoutingPoint>();
		w8 = new Wire(i3.getOutport(0), f1.getInport(0), w8rps);
//		w8.moveEdge(0, 5, 5);
//		w6.moveEdge(1, 0, 1);
//		w7.moveEdge(1, 0, 4);
//		w4.moveEdge(2, 10, 10);
//		w3.moveEdge(2, 0, -20);
//		i1.setPosition(1, 14);
//		w1.adjustForInport();
//		w1.adjustForOutport();
//		i1.setPosition(15, 14);
//		i1.setOrientation(Direction.DOWN);
//		w1.adjustForInport();
//		w1.adjustForOutport();
//		w8.moveEdge(2, 2, 0);
//		w8.moveEdge(1, 0, -5);

		try {
			em.addElement(a1);
			em.addElement(a2);
			em.addElement(a3);
			em.addElement(a4);
			em.addElement(i1);
			em.addElement(i2);
			em.addElement(i3);
			em.addElement(f1);
		} catch (DuplicateElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OverlappingElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		wm.addWire(w1);
		wm.addWire(w2);
		wm.addWire(w3);
		wm.addWire(w4);
		wm.addWire(w5);
		wm.addWire(w6);
		wm.addWire(w7);
		wm.addWire(w8);
	}
	
	public static void simple_gates_small() throws PortNumberOutOfBoundException, MaxNumberOfPortsOutOfBoundException{
		CircuitManager cm = CircuitManager.getInstance();
		WireManager wm = cm.getWireManager();
		ElementManager em = cm.getElementManager();

		AndGate a1;
		OrGate o1;
		XorGate x1;
		NandGate na1;
		InputGate i1, i2, i3;
		Wire w1, w2, w3, w4, w5, w6, w7, w8;
		Fanout f1;

		a1 = new AndGate();
		a1.setPosition(6, 15);
		o1 = new OrGate();
		o1.setPosition(43, 13);
		o1.setOrientation(Direction.UP);
		o1.setNumberOfIPs(3);
		x1 = new XorGate();
		x1.setPosition(47, 28);
		x1.setOrientation(Direction.LEFT);
		na1 = new NandGate();
		na1.setPosition(55, 20);
		i1 = new InputGate();
		i1.setPosition(0, 14);
		i2 = new InputGate();
		i2.setPosition(15, 28);
		i3 = new InputGate();
		i3.setPosition(60, 28);
		i3.setOrientation(Direction.LEFT);
		f1 = new Fanout();
		f1.setPosition(56, 30);
		w1 = new Wire(i1.getOutport(0), a1.getInport(0),
				new LinkedList<RoutingPoint>());
		LinkedList<RoutingPoint> w2rps = new LinkedList<RoutingPoint>();
		w2rps.addLast(new RoutingPoint(23, 30));
		w2rps.addLast(new RoutingPoint(23, 25));
		w2rps.addLast(new RoutingPoint(3, 25));
		w2rps.addLast(new RoutingPoint(3, 18));
		w2 = new Wire(i2.getOutport(0), a1.getInport(1), w2rps);
		LinkedList<RoutingPoint> w3rps = new LinkedList<RoutingPoint>();
		w3rps.addLast(new RoutingPoint(33, 17));
		w3rps.addLast(new RoutingPoint(33, 25));
		w3rps.addLast(new RoutingPoint(44, 25));
		w3 = new Wire(a1.getOutport(0), o1.getInport(0), w3rps);
		LinkedList<RoutingPoint> w4rps = new LinkedList<RoutingPoint>();
		w4rps.addLast(new RoutingPoint(45, 3));
		w4rps.addLast(new RoutingPoint(50, 3));
		w4rps.addLast(new RoutingPoint(50, 26));
		w4rps.addLast(new RoutingPoint(46, 26));
		w4 = new Wire(o1.getOutport(0), o1.getInport(2), w4rps);
		LinkedList<RoutingPoint> w5rps = new LinkedList<RoutingPoint>();
		w5rps.addLast(new RoutingPoint(45, 30));
		w5 = new Wire(x1.getOutport(0), o1.getInport(1), w5rps);
		LinkedList<RoutingPoint> w6rps = new LinkedList<RoutingPoint>();
		w6rps.addLast(new RoutingPoint(56, 29));
		w6 = new Wire(f1.getOutport(0), x1.getInport(0), w6rps);
		LinkedList<RoutingPoint> w7rps = new LinkedList<RoutingPoint>();
		w7rps.addLast(new RoutingPoint(56, 31));
		w7 = new Wire(f1.getOutport(1), x1.getInport(1), w7rps);
		LinkedList<RoutingPoint> w8rps = new LinkedList<RoutingPoint>();
		w8 = new Wire(i3.getOutport(0), f1.getInport(0), w8rps);

		try {
			em.addElement(a1);
			em.addElement(o1);
			em.addElement(x1);
			em.addElement(na1);
			em.addElement(i1);
			em.addElement(i2);
			em.addElement(i3);
			em.addElement(f1);
		} catch (DuplicateElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OverlappingElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wm.addWire(w1);
		wm.addWire(w2);
		wm.addWire(w3);
		wm.addWire(w4);
		wm.addWire(w5);
		wm.addWire(w6);
		wm.addWire(w7);
		wm.addWire(w8);
	}
	
	public static void env_gate() throws PortNumberOutOfBoundException, MaxNumberOfPortsOutOfBoundException{
		CircuitManager cm = CircuitManager.getInstance();
		WireManager wm = cm.getWireManager();
		ElementManager em = cm.getElementManager();
		
		EnvironmentGate e1;
		NotGate n1;
		Wire w1, w2;
		
		e1 = new EnvironmentGate();
		e1.setPosition(10, 10);
		n1 = new NotGate();
		n1.setPosition(10, 20);
		n1.setOrientation(Direction.LEFT);
		
		Protocol p = new Protocol();
		
		assert p.setString("loop 10 (<10, X> <5, 1>)");
		
		e1.getProperty().addKey(ElementPropertyKey.PROTOCOL);
		try {
			e1.getProperty().setProperty(ElementPropertyKey.PROTOCOL, p);
		} catch (InvalidPropertyException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (NoKeyException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		LinkedList<RoutingPoint> w1rps = new LinkedList<RoutingPoint>();
		w1rps.addLast(new RoutingPoint(16, 12));
		w1rps.addLast(new RoutingPoint(16, 22));
		w1 = new Wire(e1.getOutport(0), n1.getInport(0),
				w1rps);
		
		
		LinkedList<RoutingPoint> w2rps = new LinkedList<RoutingPoint>();
		w2rps.addLast(new RoutingPoint(8, 22));
		w2rps.addLast(new RoutingPoint(8, 12));
		w2 = new Wire(n1.getOutport(0), e1.getInport(0),
				w2rps);
		
		try {
			em.addElement(n1);
			em.addElement(e1);
		} catch (DuplicateElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OverlappingElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		wm.addWire(w1);
		wm.addWire(w2);
	}
}
