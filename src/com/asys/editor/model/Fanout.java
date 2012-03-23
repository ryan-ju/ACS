/**
 * 
 */
package com.asys.editor.model;

import com.asys.constants.Constant;
import com.asys.constants.LogicValue;

/**
 * @author ryan
 * 
 */
public class Fanout extends Element {
	LogicValue output = null;
	
	public Fanout() {
		super(Constant.getDefaultProperty(), 1, 2, 0, false, true);
	}

	@Override
	protected void updateDimension(){
		h = 0;
		w = 0;
		super.updatePositionOfPorts();
	}
	
	@Override
	public void setPositionOfInport(Inport ip){
		int i = getInports().indexOf(ip);
		if (i >= 0) {
			ip.setPosition(getPosition());
		}
	}
	
	@Override
	public void setPositionOfOutport(Outport op){
		int i = getOutports().indexOf(op);
		if (i >= 0) {
			op.setPosition(getPosition());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		this.output = this.getInputs().get(0);
	}
	
	@Override
	public LogicValue getOutput(){
		return output;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.asys.editor.model.Element#accept(com.asys.editor.model.ElementVisitor
	 * )
	 */
	@Override
	public void accept(ElementVisitor ev) {
		ev.visit(this);
	}

	@Override
	public Element copy() {
		Fanout f_cp = new Fanout();
		Element.copy(this, f_cp);
		return f_cp;
	}

}
