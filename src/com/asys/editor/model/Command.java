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
 * CREATE_FANOUT	wire: Wire
 * 					index: int
 * 					x: int
 * 					y: int
 * 
 * CREATE_WIRE		op: Outport
 * 					ip: Inport
 * 					rps: LinkedList<RoutingPoint>
 * 
 * CREATE_WIRE_EDGE	wire: Wire
 * 					index: int
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
 * CHANGE_NUMBER_OF_INPORT:		numIPs: int
 * 								
 * CHANGE_NUMBER_OF_OUTPORT:	numOPs: int
 * 								
 * ROTATE			isClockWise: boolean - if the rotation is clockwise 
 * 
 * SELECT_WIRE		wire: Wire - the Wire to be selected
 * 
 * SELECT_WIRE_EDGE	edge: WireEdge - the WireEdge to be selected
 * 
 * SELECT_ELEMENT	elt: Element - the Element to be selected
 * 
 * SELECT_MULTI_ELEMENT	elt: Element - the Element to be taken xor with the selected Elements
 * 
 * SELECT_GROUP_ELEMENT	elts: List<Element> - the list of Elements to be selected.  Previous selection will be deselected.
 * 
 * SELECT_GROUP_MULTI_ELEMENT	elts: List<Element> - the list of elements to be added to the selection.
 * 
 * DESELECT_ELEMENT	elt: Element - the Element to be deselected
 * 
 * SELECT_GROUP_ELEMENT	elts: ArrayList<Element> - the Elements to be selected
 * 
 * DESELECT			
 * 
 * UNDO				
 * 
 * REDO				
 * 
 * COPY
 * 
 * PASTE				x: int - the x-coordinate to be pasted at
 * 						y: int - the y-coordinate to be pasted at
 *
 */
public class Command {
	private static Command CMD;
	private CommandName name;
	private Object[] params;
	
	public static Command getInstance(){
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
