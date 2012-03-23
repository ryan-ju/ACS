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
public class NotGate extends Element {
	LogicValue output = null;
	
	public NotGate(){
		super(Constant.getDefaultProperty(), 1, 1, Constant.DEFAULT_GATE_WIDTH, false, false);
	}
	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#copy()
	 */
	@Override
	public Element copy() {
		NotGate not_cp = new NotGate();
		Element.copy(this, not_cp);
		return not_cp;
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		this.output = LogicValue.not(getInputs());
	}
	
	@Override
	public LogicValue getOutput(){
		return output;
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#accept(com.asys.editor.model.ElementVisitor)
	 */
	@Override
	public void accept(ElementVisitor ev) {
		ev.visit(this);
	}

}
