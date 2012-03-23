/**
 * 
 */
package com.asys.utils;

import com.asys.editor.model.Element;
import com.asys.editor.model.Point;
import com.asys.editor.model.Wire;
import com.asys.editor.model.WireEdge;


/**
 * @author ryan
 * 
 */
public class Decider {

	/**
	 * On a number line, decide if the line (a1, a2) overlaps the line (b1, b2).
	 * The numbers need not be in order.
	 * 
	 * @param a1
	 * @param a2
	 * @param b1
	 * @param b2
	 * @param atEndPoint
	 *            true - overlapping at end points also returns true.
	 * @return true - if there is overlapping
	 */
	public static boolean overlap(int a1, int a2, int b1, int b2,
			boolean atEndPoint) {
		int min_a = Math.min(a1, a2);
		int max_a = Math.max(a1, a2);
		int min_b = Math.min(b1, b2);
		int max_b = Math.max(b1, b2);
		if (atEndPoint) {
			return (min_a <= max_b && min_b <= max_a);
		} else {
			return (min_a < max_b && min_b < max_a);
		}
	}
	
	public static boolean isPointInsideElement(Point p, Element elt){
		return elt.getX()<p.getX() && p.getX()<elt.getX()+elt.getWidth() && elt.getY()<p.getY() && p.getY()<elt.getY()+elt.getHeight();
	}
	
	public static boolean isPointOnWireEdge(Point p, WireEdge edge){
		return WireEdge.isOnWireEdge(p, edge);
	}
	
	public static boolean isPointOnWire(Point p, Wire wire){
		for (WireEdge edge:wire.getRoutingEdges()){
			if (isPointOnWireEdge(p, edge)){
				return true;
			}
		}
		return false;
	}

}
