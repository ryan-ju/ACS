/**
 * 
 */
package com.asys.editor.model;

import java.util.List;

import com.asys.constants.Constant;
import com.asys.constants.LogicValue;

/**
 * @author ryan
 *
 */
public class CGate extends Element {
	LogicValue output = null;
	
	public CGate(){
		super(Constant.getDefaultProperty(), 2, 1, 4, true, false);
	}
	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#copy()
	 */
	@Override
	public Element copy() {
		CGate c_cp = new CGate();
		Element.copy(this, c_cp);
		return c_cp;
	}

	/* (non-Javadoc)
	 * @see com.asys.editor.model.Element#evaluate()
	 */
	@Override
	public void evaluate() {
		List<LogicValue> inputs = this.getInputs();
		assert inputs.size() == 2;
		if (inputs.get(0)==LogicValue.ONE && inputs.get(1) == LogicValue.ONE){
			output = LogicValue.ONE;
		}else if (inputs.get(0)==LogicValue.ZERO && inputs.get(1) == LogicValue.ZERO){
			output = LogicValue.ZERO;
		}else{
			// TODO This method is troublesome, and should never be used.
		}
//		ArrayList<LogicValue> inputs = new ArrayList<LogicValue>();
//		inputs.addAll(getInputs());
//		inputs.add(getOutput());
//		output = LogicValue.majority(inputs);
	}

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
