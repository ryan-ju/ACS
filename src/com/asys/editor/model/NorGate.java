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
public class NorGate extends Element {
	LogicValue output = null;
	
	public NorGate(){
		super(Constant.getDefaultProperty(), 2, 1, Constant.DEFAULT_GATE_WIDTH, true, false);
	}
	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#copy()
	 */
	@Override
	public Element copy() {
		NorGate nor_cp = new NorGate();
		Element.copy(this, nor_cp);
		return nor_cp;
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		this.output = LogicValue.nor(getInputs());
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
