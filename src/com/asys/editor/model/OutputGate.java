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
public class OutputGate extends Element {
	LogicValue output = null;

	public OutputGate() {
		super(Constant.getDefaultProperty(), 1, 0, 4, false, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asys.editor.model.Element#copy()
	 */
	@Override
	public Element copy() {
		OutputGate output_cp = new OutputGate();
		Element.copy(this, output_cp);
		return output_cp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		this.output = getInputs().get(0);
	}

	@Override
	public LogicValue getOutput() {
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

}
