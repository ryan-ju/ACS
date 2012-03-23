/**
 * 
 */
package com.asys.model.components.exceptions;

import com.asys.editor.model.RoutingPoint;

/**
 * @author ryan
 *
 */
public class InvalidRoutingPointException extends Exception {
	private RoutingPoint rp1, rp2;
	
	public InvalidRoutingPointException(RoutingPoint rp1, RoutingPoint rp2){
		this.rp1=rp1;
		this.rp2=rp2;
	}
	
	public RoutingPoint[] getRoutingPoints(){
		return new RoutingPoint[]{rp1,rp2};
	}
}
