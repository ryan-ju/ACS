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
public class InputGate extends Element {
	LogicValue output = null;
	
	public InputGate() {
		super(Constant.getDefaultProperty(), 0, 1, 4, false, false);
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		// TODO Auto-generated method stub
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
	
	@Override
	public Element copy() {
		InputGate ig_cp = new InputGate();
		Element.copy(this, ig_cp);
		return ig_cp;
	}

}
