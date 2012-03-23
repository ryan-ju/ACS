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
public class AndGate extends Element {
	LogicValue output = null;
	
	public AndGate(){
		super(Constant.getDefaultProperty(), 2, 1, Constant.DEFAULT_GATE_WIDTH, true, false);
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		this.output = LogicValue.and(getInputs());
	}
	
	@Override
	public LogicValue getOutput() {
		return LogicValue.and(getInputs());
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#accept(com.asys.editor.model.ElementVisitor)
	 */
	@Override
	public void accept(ElementVisitor ev) {
		ev.visit(this);
	}

	@Override
	public Element copy() {
		AndGate and_cp = new AndGate();
		Element.copy(this, and_cp);
		return and_cp;
	}
}
