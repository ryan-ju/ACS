/**
 * 
 */
package com.asys.editor.model;

import com.asys.constants.CommandName;

/**
 * Types of Commands and parameters:
 * 
 * CommandName		Parameter(s)
 * 
 * CREATE_GATE		x: int - the x-coordinate of the new element
 * 					y: int - the y-coordinate of the new element
 * 
 * CREATE_FANOUT	edge: WireEdge
 * 					x: int
 * 					y: int
 * 
 * CREATE_WIRE		op: Outport
 * 					ip: Inport
 * 					rps: LinkedList<RoutingPoint>
 * 
 * CREATE_WIRE_EDGE	edge: WireEdge
 * 					x1: int
 * 					y1: int
 * 					x2: int
 * 					y2: int
 * 
 * DELETE			
 * 
 * MOVE				dx: int - the x displacement
 * 					dy: int - the y displacement
 * 
 * POINT_SELECT		x: int
 * 					y: int
 * 
 * RANGE_SELECT		x: int
 * 					y: int
 * 					w: int
 * 					h: int
 * 
 * UNDO				
 * 
 * REDO				
 * 
 * COPY
 * 
 * PASTE
 *
 */
public class Command {
	private static Command CMD;
	private CommandName name;
	private Object[] params;
	
	public Command getInstance(){
		if (CMD == null){
			CMD = new Command();
		}
		return CMD;
	}
	
	public CommandName getCommandName(){
		return name;
	}
	
	public void setCommandName(CommandName name){
		this.name = name;
	}
	
	public Object[] getParams(){
		return params;
	}
	
	public Object getParam(int i){
		return params[i];
	}
	
	public void setParams(Object[] params){
		this.params = params;
	}
}
