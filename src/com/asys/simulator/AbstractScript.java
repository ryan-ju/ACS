/**
 * 
 */
package com.asys.simulator;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.asys.constants.LogicValue;

/**
 * @author ryan
 * 
 */
public abstract class AbstractScript {
	private LinkedList<DelayValuePair> history = new LinkedList<DelayValuePair>();

	abstract DelayValuePair getNextPair();

	/**
	 * Restoring all children Loops to their initial value. This is for starting
	 * a new outer Loop.
	 */
	abstract public void restore();

	public DelayValuePair getNextPairLogging() {
		DelayValuePair temp = getNextPair();
		if (temp != null) {
			history.addLast(temp);
		}
		return temp;
	}
	
	public void restoreCleanseHistory(){
		this.restore();
		history.clear();
	}
	
	public String historyToString(){
		StringBuffer buffer = new StringBuffer();
		for (DelayValuePair pair:history){
			buffer.append(pair.toString());
		}
		return buffer.toString();
	}

	// ====================================================
	// AbstractScriptParser
	// ====================================================

	public static AbstractScript parse(String string) {
		if (isValid(string)) {
			return subParse(string);
		} else {
			return null;
		}
	}

	public static boolean isValid(String string) {
		string = string.trim();
		int i = isFirstOrSecondType(string);
		int j = isThirdorFourthType(string);
		System.out.println("i==" + i + ", j==" + j);
		System.out
				.println("\""
						+ string
						+ "\" is "
						+ (i > 0 ? "type \"loop n (S) S\""
								: (i == -1 ? "type \"loop n (S)\""
										: (j > 0 ? "type \"<long, LogicValue> S\""
												: (j == -1 ? "type \"<long, LogicValue>\""
														: "Invalid")))));
		System.out.println();
		if (((i > 0 || i == -1) && j == 0) || (i == 0 && (j > 0 || j == -1))) {
			if (i > 0) { // Type "loop n (S) S"
				return isValid(string.substring(0, i))
						&& isValid(string.substring(i));
			} else if (i == -1) { // Type "loop n (S)"

				Pattern num_loop_pattern = Pattern
						.compile("loop\\s+[0-9]+\\s*");
				Matcher num_loop_matcher = num_loop_pattern.matcher(string);
				num_loop_matcher.find();
				int after_loop_num_index;
				try {
					after_loop_num_index = num_loop_matcher.end();
				} catch (IllegalStateException e) {
					return false;
				}

				String substring = string.substring(after_loop_num_index + 1,
						string.length() - 1);
				System.out.println("\"" + string + "\" has substring \""
						+ substring + "\"");
				return isValid(substring);
			} else if (j > 0) { // Type "<long, LogicValue> S"
				return isValid(string.substring(0, j))
						&& isValid(string.substring(j));
			} else { // Type "<long, LogicValue>"
				assert j == -1;
				return Pattern.matches("<\\s*[0-9]+\\s*,\\s*[01xX]\\s*>",
						string);
			}
		} else {
			return false;
		}
	};

	/**
	 * A recursive method employing a divide and conquer idea.
	 * 
	 * The input string has the following CFG: S = loop n (S) | loop n (S) S |
	 * <long, LogicValue> S | <long, LogicValue>
	 * 
	 * @param input
	 *            - of type {String, Map<String, AbstractScript>}
	 * @return
	 */
	private static AbstractScript subParse(String input) {

		String str = input.trim();

		Pattern single_pair_pattern = Pattern
				.compile("<\\s*[0-9]+\\s*,\\s*[01xX]\\s*>"); // Eg, %var1%
		Matcher single_pair_matcher = single_pair_pattern.matcher(str);
		if (single_pair_matcher.matches()) { // Type "<long, LogicValue>"
			// Split the input string into two parts
			String delims = ",\\s*";
			String[] tokens = str.split(delims);
			assert tokens.length == 2;

			// Match the delay
			Pattern number_pattern = Pattern.compile("[0-9]+");
			Matcher number_matcher = number_pattern.matcher(tokens[0]);
			number_matcher.find();
			String delay_string = number_matcher.group();
			long delay = Long.parseLong(delay_string);

			// Match the logic value
			String second_part = tokens[1];
			// Try to get the very first character of second_part
			String logic_string = second_part.substring(0, 1);
			LogicValue logic_value = getLogicValue(logic_string);
			DelayValuePair delay_value_pair = new DelayValuePair(delay,
					logic_value);

			return new Single(delay_value_pair);
		} else {
			int i = isFirstOrSecondType(str);
			if (i < 0) { // Type "long n (S)"
				int first_bracket = str.indexOf("(");
				AbstractScript feedback = subParse(str.substring(first_bracket+1,
						str.length() - 1));

				Pattern num_loop_pattern = Pattern.compile("[0-9]+");
				Matcher num_loop_matcher = num_loop_pattern.matcher(str);
				num_loop_matcher.find();
				String num_loop_str = num_loop_matcher.group();
				long num_loop = Long.parseLong(num_loop_str);

				return new Loop(num_loop, feedback);
			} else if (i > 0) { // Type "loop n (S) S"
				AbstractScript left = subParse(str.substring(0, i));
				AbstractScript right = subParse(str.substring(i));
				return new Sequence(left, right);
			} else { // Type "<long, LogicValue> S"
				int j = isThirdorFourthType(str);
				System.out.println("j="+j);
				System.out.println("Script under parsing is \""+str+"\"");
				assert j > 0;
				AbstractScript left = subParse(str.substring(0, j));
				AbstractScript right = subParse(str.substring(j));
				return new Sequence(left, right);
			}
		}
	}

	/**
	 * Get the logic value corresponding to l
	 * 
	 * @param str
	 * @return
	 */
	private static LogicValue getLogicValue(String str) {
		assert str.length() == 1;
		assert str.equals("0") || str.equals("1")
				|| str.toLowerCase().equals("x");
		if (str.equals("0")) {
			return LogicValue.ZERO;
		} else if (str.equals("1")) {
			return LogicValue.ONE;
		} else {
			return LogicValue.X;
		}
	}

	/**
	 * Test if the input string is of type "loop n (S)" or "loop n (S) S". This
	 * is done by counting brackets.
	 * 
	 * @param string
	 * @return i>0 - the input string is of type "loop n (S) S", i represents
	 *         the end index of "loop n (S)" + 1 -1 - the input string is of
	 *         type "loop n (S)" 0 - the input string is of other types
	 */
	private static int isFirstOrSecondType(String string) {
		string.trim();
		if (Pattern.matches("loop\\s+[0-9]+.+", string)) {
			char[] chars = string.toCharArray();
			int left_count = 0, right_count = 0;
			for (int i = 0; i < string.length(); i++) {
				if (left_count != 0 && left_count == right_count) {
					return i;
				}
				if (chars[i] == '(') {
					left_count++;
				} else if (chars[i] == ')') {
					right_count++;
				}
				assert left_count >= right_count;
			}
			assert left_count == right_count;
			return -1;
		}
		return 0;
	}

	/**
	 * Test if the input string is of type "<long, LogicValue>" or
	 * "<long, LogicValue> S". This is done by counting brackets.
	 * 
	 * @param string
	 * @return -1 - the input string is of type "<long, LogicValue>" i>0 - the
	 *         input string is of type "<long, LogicValue> S", i represents the
	 *         end index of "<long, LogicValue>" + 1
	 */
	private static int isThirdorFourthType(String string) {
		string.trim();
		Pattern single_pattern = Pattern
				.compile("<\\s*[0-9]+\\s*,\\s*[01xX]\\s*>");
		Matcher single_matcher = single_pattern.matcher(string);
		if (single_matcher.find() && single_matcher.start() == 0) {
			int end_index = single_matcher.end();
			if (end_index == string.length()) {
				return -1;
			} else {
				return end_index;
			}
		} else {
			return 0;
		}
	}

	// ====================================================
	// Inner classes
	// ====================================================

	static class Loop extends AbstractScript {
		private long n, m;
		private AbstractScript child;

		Loop(long n, AbstractScript child) {
			assert n > 0;
			this.n = n;
			this.m = n;
			this.child = child;
		}

		public long getTotalLoopCount() {
			return n;
		}

		public long getLoopCountDown() {
			return m;
		}

		public AbstractScript getChild() {
			return child;
		}

		@Override
		DelayValuePair getNextPair() {
			DelayValuePair temp = child.getNextPair();
			if (temp == null){
				m--;
				if (m<=0){
					return null;
				}else{
					child.restore();
					temp = child.getNextPair();
					assert temp != null;
					return temp;
				}
			}
			else{
				return temp;
			}
		}

		@Override
		public void restore() {
			this.m = n;
			child.restore();
		}

	}

	static class Sequence extends AbstractScript {
		private AbstractScript left, right;

		Sequence(AbstractScript left, AbstractScript right) {
			this.left = left;
			this.right = right;
		}

		@Override
		DelayValuePair getNextPair() {
			DelayValuePair temp = left.getNextPair();
			if (temp == null) {
				temp = right.getNextPair();
			}
			return temp;
		}

		@Override
		public void restore() {
			left.restore();
			right.restore();
		}

	}

	static class Single extends AbstractScript {
		private boolean visited;
		private DelayValuePair pair;

		Single(DelayValuePair pair) {
			this.pair = new DelayValuePair(pair);
		}

		public DelayValuePair getDelayValuePair() {
			return pair;
		}

		@Override
		DelayValuePair getNextPair() {
			if (visited)
				return null;
			else {
				visited = true;
				return pair;
			}
		}

		@Override
		public void restore() {
			this.visited = false;
		}
	}

	static class DelayValuePair {
		private long delay;
		private LogicValue value;

		DelayValuePair(long delay, LogicValue value) {
			this.delay = delay;
			this.value = value;
		}

		DelayValuePair(DelayValuePair pair) {
			this.delay = pair.getDelay();
			this.value = pair.getLogicValue();
		}

		public long getDelay() {
			return delay;
		}

		public LogicValue getLogicValue() {
			return value;
		}
		
		@Override
		public String toString(){
			return "<"+delay+","+value.toString()+">";
		}
	}
}
