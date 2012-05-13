/**
 * 
 */
package com.asys.simulator;

import java.util.ArrayList;
import java.util.List;

import com.asys.constants.LogicValue;

/**
 * @author ryan
 * 
 */
public abstract class EvaluationStrategy {

	abstract public LogicValue evaluate(List<LogicValue> inputs,
			LogicValue output);

	/*
	 * Evaluation strategy for Input.
	 */
	private class InputEvaluationStrategy extends EvaluationStrategy {
		private LogicValue value;

		protected void setValue(LogicValue value) {
			this.value = value;
		}

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return value;
		}

	}

	/*
	 * Evaluation strategy for Output.
	 */
	private class OutputEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return null;
		}

	}

	/*
	 * Evaluation strategy for Environment.
	 */
	private class EnvironmentEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	/*
	 * Evaluation strategy for Not gate.
	 */
	private class NotEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			assert inputs.size() == 1;
			return LogicValue.not(inputs.get(0));
		}

	}

	/*
	 * Evaluation strategy for And gate.
	 */
	private class AndEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.and(inputs);
		}

	}

	/*
	 * Evaluation strategy for Or gate.
	 */
	private class OrEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.or(inputs);
		}

	}

	/*
	 * Evaluation strategy for Nand gate.
	 */
	private class NandEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.nand(inputs);
		}

	}

	/*
	 * Evaluation strategy for Nor gate.
	 */
	private class NorEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.nor(inputs);
		}

	}

	/*
	 * Evaluation strategy for Xor gate.
	 */
	private class XorEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.xor(inputs);
		}

	}

	/*
	 * Evaluation strategy for Majority gate.
	 */
	private class MajorityEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			return LogicValue.majority(inputs);
		}

	}

	/*
	 * Evaluation strategy for Majority gate.
	 */
	private class CEvaluationStrategy extends EvaluationStrategy {

		@Override
		public LogicValue evaluate(List<LogicValue> inputs, LogicValue output) {
			ArrayList<LogicValue> temp_inputs = new ArrayList<LogicValue>(
					inputs);
			temp_inputs.add(output);
			return LogicValue.majority(temp_inputs);
		}

	}

}
