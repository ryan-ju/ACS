/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.List;

import com.asys.constants.LogicValue;
import com.asys.editor.model.AndGate;
import com.asys.editor.model.CGate;
import com.asys.editor.model.Element;
import com.asys.editor.model.EnvironmentGate;
import com.asys.editor.model.InputGate;
import com.asys.editor.model.NandGate;
import com.asys.editor.model.NorGate;
import com.asys.editor.model.NotGate;
import com.asys.editor.model.OrGate;
import com.asys.editor.model.OutputGate;
import com.asys.editor.model.XorGate;

/**
 * @author ryan
 * 
 */
public abstract class EvaluationStrategy {
	private static InputEvaluationStrategy input_es;
	private static OutputEvaluationStrategy output_es;
	private static EnvironmentEvaluationStrategy env_es;
	private static NotEvaluationStrategy not_es;
	private static AndEvaluationStrategy and_es;
	private static OrEvaluationStrategy or_es;
	private static NandEvaluationStrategy nand_es;
	private static NorEvaluationStrategy nor_es;
	private static XorEvaluationStrategy xor_es;
	private static CEvaluationStrategy c_es;

	abstract public LogicValue evaluate(List<LogicValue> inputs,
			LogicValue output);

	public static EvaluationStrategy getEvaluationStrategy(Element elt) {
		if (elt instanceof InputGate) {
			if (input_es == null) {
				input_es = new InputEvaluationStrategy();
			}
			return input_es;
		} else if (elt instanceof OutputGate) {
			if (output_es == null) {
				output_es = new OutputEvaluationStrategy();
			}
			return output_es;
		} else if (elt instanceof EnvironmentGate) {
			if (env_es == null) {
				env_es = new EnvironmentEvaluationStrategy();
			}
			return env_es;
		} else if (elt instanceof NotGate) {
			if (not_es == null) {
				not_es = new NotEvaluationStrategy();
			}
			return not_es;
		} else if (elt instanceof AndGate) {
			if (and_es == null) {
				and_es = new AndEvaluationStrategy();
			}
			return and_es;
		} else if (elt instanceof OrGate) {
			if (or_es == null) {
				or_es = new OrEvaluationStrategy();
			}
			return or_es;
		} else if (elt instanceof NandGate) {
			if (nand_es == null) {
				nand_es = new NandEvaluationStrategy();
			}
			return nand_es;
		} else if (elt instanceof NorGate) {
			if (nor_es == null) {
				nor_es = new NorEvaluationStrategy();
			}
			return nor_es;
		} else if (elt instanceof XorGate) {
			if (xor_es == null) {
				xor_es = new XorEvaluationStrategy();
			}
			return xor_es;
		} else if (elt instanceof CGate) {
			if (c_es == null) {
				c_es = new CEvaluationStrategy();
			}
			return c_es;
		} else {
			return null;
		}
	}

	/*
	 * Evaluation strategy for Input.
	 */
	private static class InputEvaluationStrategy extends EvaluationStrategy {
		private LogicValue value;

		protected void setValue(LogicValue value) {
			this.value = value;
		}

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return output;
		}

	}

	/*
	 * Evaluation strategy for Output.
	 */
	private static class OutputEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return null;
		}

	}

	/*
	 * Evaluation strategy for Environment.
	 */
	private static class EnvironmentEvaluationStrategy extends
			EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/*
	 * Evaluation strategy for Not gate.
	 */
	private static class NotEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			assert inputs.size() == 1;
			return LogicValue.not(inputs.get(0));
		}

	}

	/*
	 * Evaluation strategy for And gate.
	 */
	private static class AndEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.and(inputs);
		}

	}

	/*
	 * Evaluation strategy for Or gate.
	 */
	private static class OrEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.or(inputs);
		}

	}

	/*
	 * Evaluation strategy for Nand gate.
	 */
	private static class NandEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.nand(inputs);
		}

	}

	/*
	 * Evaluation strategy for Nor gate.
	 */
	private static class NorEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.nor(inputs);
		}

	}

	/*
	 * Evaluation strategy for Xor gate.
	 */
	private static class XorEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.xor(inputs);
		}

	}

	/*
	 * Evaluation strategy for Majority gate.
	 */
	private static class CEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			assert inputs.size() == 2;

			if (inputs.get(0) == inputs.get(1)) {
				return inputs.get(0);
			} else {
				return null;
			}
		}

	}

}
