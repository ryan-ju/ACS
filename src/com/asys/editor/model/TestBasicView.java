/**
 * 
 */
package com.asys.editor.model;

import java.util.LinkedList;

import com.asys.constants.Direction;
import com.asys.model.components.exceptions.PortNumberOutOfBoundException;
import com.asys.views.BasicViewer;

/**
 * @author ryan
 *
 */
public class TestBasicView {
	public static void main(String[] args) throws PortNumberOutOfBoundException {
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
		w4rps.addLast(new RoutingPoint(45,3));
		w4rps.addLast(new RoutingPoint(50,3));
		w4rps.addLast(new RoutingPoint(50,26));
		w4rps.addLast(new RoutingPoint(46,26));
		w4 = new Wire(a2.getOutport(0), a2.getInport(1), w4rps);
		
		w2.moveEdge(2, 0, -8);
//		w2.moveEdge(0, 0, -5);
		w2.moveEdge(1, 21, 0);
		
		em.addElement(a1);
		em.addElement(a2);
		em.addElement(i1);
		em.addElement(i2);
		
		wm.addWire(w1);
		wm.addWire(w2);
		wm.addWire(w3);
		wm.addWire(w4);
		
		BasicViewer bv = new BasicViewer(CircuitManager.getInstance());
		bv.setVisible(true);
	}
}
