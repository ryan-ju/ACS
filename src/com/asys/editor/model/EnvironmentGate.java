/**
 * 
 */
package com.asys.editor.model;

import com.asys.constants.Constant;
import com.asys.constants.ElementPropertyKey;
import com.asys.constants.LogicValue;

/**
 * @author ryan
 *
 */
public class EnvironmentGate extends Element {
	LogicValue output = null;
	
	public EnvironmentGate(){
		super(Constant.getDefaultProperty(), 1, 1, 4, false, false);
		this.getProperty().addKey(ElementPropertyKey.PROTOCOL);
	}
	
	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#copy()
	 */
	@Override
	public Element copy() {
		EnvironmentGate env_cp = new EnvironmentGate();
		Element.copy(this, env_cp);
		return env_cp;
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#getOutput()
	 */
	@Override
	public LogicValue getOutput() {
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
