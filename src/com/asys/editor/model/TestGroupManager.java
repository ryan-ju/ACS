/**
 * 
 */
package com.asys.editor.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author ryan
 * 
 */
public class TestGroupManager {
	private static GroupManager gm;
	private static AndGate g1, g2, g3, g4, g5, g6, g7, g8, g9, g10;
	private static InputGate i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12,
			i13, i14;
	private static Wire w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, w13,
			w14, w15, w16, w17, w18, w19, w20, w21, w22, w23, w24;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		gm = new GroupManager(null);
		i1 = new InputGate();
		i2 = new InputGate();
		i3 = new InputGate();
		i4 = new InputGate();
		i5 = new InputGate();
		i6 = new InputGate();
		i7 = new InputGate();
		i8 = new InputGate();
		i9 = new InputGate();
		i10 = new InputGate();
		i11 = new InputGate();
		i12 = new InputGate();
		i13 = new InputGate();
		i14 = new InputGate();
		g1 = new AndGate();
		g2 = new AndGate();
		g3 = new AndGate();
		g4 = new AndGate();
		g5 = new AndGate();
		g6 = new AndGate();
		g7 = new AndGate();
		g8 = new AndGate();
		g9 = new AndGate();
		g10 = new AndGate();
		Method setMaxIPs = Element.class.getDeclaredMethod("setMaxIPs",
				new Class[] { int.class });
		setMaxIPs.setAccessible(true);
		setMaxIPs.invoke(g9, new Object[] { new Integer(3) });
		System.out.println("g9.getMaxIPs()==" + g9.getMaxIPs());
		setMaxIPs.invoke(g10, new Object[] { new Integer(5) });
		w1 = new Wire(i1.getOutport(0), g1.getInport(0), null);
		w2 = new Wire(i2.getOutport(0), g1.getInport(1), null);
		w3 = new Wire(i3.getOutport(0), g2.getInport(0), null);
		w4 = new Wire(i4.getOutport(0), g2.getInport(1), null);
		w5 = new Wire(i5.getOutport(0), g3.getInport(0), null);
		w6 = new Wire(i6.getOutport(0), g3.getInport(1), null);
		w7 = new Wire(i7.getOutport(0), g4.getInport(0), null);
		w8 = new Wire(i8.getOutport(0), g4.getInport(1), null);
		w9 = new Wire(g1.getOutport(0), g5.getInport(0), null);
		w10 = new Wire(g2.getOutport(0), g5.getInport(1), null);
		w11 = new Wire(g3.getOutport(0), g9.getInport(1), null);
		w12 = new Wire(g4.getOutport(0), g9.getInport(2), null);
		w13 = new Wire(g5.getOutport(0), g9.getInport(0), null);
		w14 = new Wire(i9.getOutport(0), g6.getInport(0), null);
		w15 = new Wire(i10.getOutport(0), g6.getInport(1), null);
		w16 = new Wire(i11.getOutport(0), g7.getInport(0), null);
		w17 = new Wire(i12.getOutport(0), g7.getInport(1), null);
		w18 = new Wire(i13.getOutport(0), g8.getInport(0), null);
		w19 = new Wire(i14.getOutport(0), g8.getInport(1), null);
		w20 = new Wire(g6.getOutport(0), g10.getInport(0), null);
		w21 = new Wire(g7.getOutport(0), g10.getInport(1), null);
		w22 = new Wire(g8.getOutport(0), g10.getInport(2), null);
		w23 = new Wire(g9.getOutport(0), g10.getInport(3), null);
		w24 = new Wire(g10.getOutport(0), g10.getInport(4), null);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		gm.deselectAll();
	}

	@Test
	public void group1Test() {
		ArrayList<Element> elts = new ArrayList<Element>();
		elts.add(g1);
		elts.add(g2);
		elts.add(g3);
		elts.add(g4);
		gm.setSelection(elts);
		List<Wire> wires = gm.getInducedWires();
		assert wires.isEmpty();
	}

	@Test
	public void group2Test() {
		ArrayList<Element> elts = new ArrayList<Element>();
		elts.add(g9);
		gm.setSelection(elts);
		List<Wire> wires = gm.getInducedWires();
		assert wires.isEmpty();
	}

	@Test
	public void group3Test() {
		ArrayList<Element> elts = new ArrayList<Element>();
		elts.add(g6);
		elts.add(g7);
		elts.add(g10);
		gm.setSelection(elts);
		List<Wire> wires = gm.getInducedWires();
		System.out.println("For group3, wires.size() = "+wires.size());
		assert wires.size() == 3;
		assert wires.contains(w20);
		assert wires.contains(w21);
		assert wires.contains(w24);
	}
	
	@Test
	public void group4Test(){
		ArrayList<Element> elts = new ArrayList<Element>();
		elts.add(g6);
		elts.add(g7);
		elts.add(g8);
		elts.add(g9);
		elts.add(g10);
		elts.add(i9);
		elts.add(i10);
		elts.add(i11);
		elts.add(i12);
		elts.add(i13);
		elts.add(i14);
		gm.setSelection(elts);
		List<Wire> wires = gm.getInducedWires();
		System.out.println("For group4, wires.size() = "+wires.size());
		assert wires.contains(w14);
		assert wires.contains(w15);
		assert wires.contains(w16);
		assert wires.contains(w17);
		assert wires.contains(w18);
		assert wires.contains(w19);
		assert wires.contains(w20);
		assert wires.contains(w21);
		assert wires.contains(w22);
		assert wires.contains(w23);
		assert wires.contains(w24);
	}

}
