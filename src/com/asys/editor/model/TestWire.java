package com.asys.editor.model;

import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.asys.constants.Direction;
import com.asys.views.BasicViewer;

public class TestWire {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		CircuitManager cm = CircuitManager.getInstance();
		WireManager wm = cm.getWireManager();
		ElementManager em = cm.getElementManager();

		AndGate a1, a2;
		InputGate i1, i2;
		Wire w1, w2, w3, w4;

		a1 = new AndGate();
		a1.setPosition(6, 15);
		a2 = new AndGate();
		a2.setPosition(43, 6);
		a2.setOrientation(Direction.UP);
		i1 = new InputGate();
		i1.setPosition(0, 14);
		i2 = new InputGate();
		i2.setPosition(15, 28);
		w1=new Wire(i1.getOutport(0),a1.getInport(0), new LinkedList<RoutingPoint>());
		LinkedList<RoutingPoint> w2rps = new LinkedList<RoutingPoint>();
		w2rps.add(new RoutingPoint(10, 18));
		w2rps.add(new RoutingPoint(10, 25));
		w2rps.add(new RoutingPoint(23, 25));
		w2rps.add(new RoutingPoint(23, 30));
		w2 = new Wire(i2.getOutport(0), a1.getInport(1), w2rps);
		LinkedList<RoutingPoint> w3rps = new LinkedList<RoutingPoint>();
		w3rps.add(new RoutingPoint(33, 8));
		w3rps.add(new RoutingPoint(33, 15));
		w3rps.add(new RoutingPoint(44, 15));
		w3 = new Wire(a1.getOutport(0), a2.getInport(0), w3rps);
		LinkedList<RoutingPoint> w4rps = new LinkedList<RoutingPoint>();
		w4rps.add(new RoutingPoint(45,3));
		w4rps.add(new RoutingPoint(50,3));
		w4rps.add(new RoutingPoint(50,16));
		w4rps.add(new RoutingPoint(46,16));
		w4 = new Wire(a2.getOutport(0), a2.getInport(1), w4rps);
		
		em.addElement(a1);
		em.addElement(a2);
		em.addElement(i1);
		em.addElement(i2);
		
		wm.addWire(w1);
		wm.addWire(w2);
		wm.addWire(w3);
		wm.addWire(w4);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWire() {
		BasicViewer bv = new BasicViewer(CircuitManager.getInstance());
		bv.setVisible(true);
	}

}
