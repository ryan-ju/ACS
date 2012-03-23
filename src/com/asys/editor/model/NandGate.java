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
public class NandGate extends Element {
	LogicValue output = null;
	public NandGate(){
		super(Constant.getDefaultProperty(), 2, 1, Constant.DEFAULT_GATE_WIDTH, true, false);
	}
	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#copy()
	 */
	@Override
	public Element copy() {
		NandGate nand_cp = new NandGate();
		Element.copy(this, nand_cp);
		return nand_cp;
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		this.output = LogicValue.nand(getInputs());
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
