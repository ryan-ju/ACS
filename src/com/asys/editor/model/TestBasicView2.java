/**
 * 
 */
package com.asys.editor.model;

import java.util.LinkedList;

import com.asys.constants.Direction;
import com.asys.model.components.exceptions.MaxNumberOfPortsOutOfBoundException;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;
import com.asys.views.BasicViewer;

/**
 * @author ryan
 * 
 */
public class TestBasicView2 {
	public static void main(String[] args)
			throws PortNumberOutOfBoundException,
			MaxNumberOfPortsOutOfBoundException {
		CircuitManager cm = CircuitManager.getInstance();
		WireManager wm = cm.getWireManager();
		ElementManager em = cm.getElementManager();

		AndGate a1, a2, a3;
		InputGate i1, i2, i3;
		Wire w1, w2, w3, w4, w5, w6, w7, w8;
		Fanout f1;

		a1 = new AndGate();
		a1.setPosition(6, 15);
		a2 = new AndGate();
		a2.setPosition(43, 13);
		a2.setOrientation(Direction.UP);
		a2.setMaxIPs(3);
		a3 = new AndGate();
		a3.setPosition(47, 28);
		a3.setOrientation(Direction.LEFT);
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
		w7 = new Wire(f1.getOutport(0), a3.getInport(1), w7rps);
		LinkedList<RoutingPoint> w8rps = new LinkedList<RoutingPoint>();
		w8 = new Wire(i3.getOutport(0), f1.getInport(0), w8rps);
		w8.moveEdge(0, 5, 5);
		w6.moveEdge(1, 0, 1);
		w7.moveEdge(1, 0, 4);
		w4.moveEdge(2, 10, 10);
		w3.moveEdge(2, 0, -20);
		i1.setPosition(1, 14);
		w1.adjustForInport();
		w1.adjustForOutport();
		i1.setPosition(15, 14);
		i1.setOrientation(Direction.DOWN);
		w1.adjustForInport();
		w1.adjustForOutport();
		w8.moveEdge(2, 2, 0);
		w8.moveEdge(1, 0, -5);

		em.addElement(a1);
		em.addElement(a2);
		em.addElement(a3);
		em.addElement(i1);
		em.addElement(i2);
		em.addElement(i3);
		em.addElement(f1);

		wm.addWire(w1);
		wm.addWire(w2);
		wm.addWire(w3);
		wm.addWire(w4);
		wm.addWire(w5);
		wm.addWire(w6);
		wm.addWire(w7);
		wm.addWire(w8);

		BasicViewer bv = new BasicViewer(CircuitManager.getInstance());
		bv.setVisible(true);
	}
}
