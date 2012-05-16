/**
 * 
 */
package com.asys.constants;

import java.util.List;

/**
 *
 */
public enum LogicValue {
	ZERO, ONE, X;

	@Override
	public String toString() {
		if (this.equals(ZERO))
			return "0";
		else if (this.equals(ONE))
			return "1";
		else if (this.equals(X))
			return "X";
		else
			return "";
	}

	public static LogicValue not(LogicValue l1) {
		if (l1 == LogicValue.ONE) {
			return LogicValue.ZERO;
		} else if (l1 == LogicValue.ZERO) {
			return LogicValue.ONE;
		} else if (l1 == LogicValue.X) {
			return LogicValue.X;
		} else {
			assert l1 == null;
			return null;
		}
	}

	public static LogicValue not(List<LogicValue> ls) {
		assert ls.size() == 1;
		LogicValue l1 = ls.get(0);
		if (l1 == LogicValue.ONE) {
			return LogicValue.ZERO;
		} else if (l1 == LogicValue.ZERO) {
			return LogicValue.ONE;
		} else if (l1 == LogicValue.X) {
			return LogicValue.X;
		} else {
			assert l1 == null;
			return null;
		}
	}

	public static LogicValue and(LogicValue l1, LogicValue l2) {
		if (l1 == null || l2 == null) {
			return null;
		} else {
			if (l1 == LogicValue.ZERO || l2 == LogicValue.ZERO) {
				return LogicValue.ZERO;
			} else if (l1 == LogicValue.ONE && l2 == LogicValue.ONE) {
				return LogicValue.ONE;
			} else {
				return LogicValue.X;
			}
		}
	}

	public static LogicValue and(List<LogicValue> ls) {
		boolean xthere = false;
		for (LogicValue lv : ls) {
			if (lv == null) {
				return null;
			}
			if (lv == LogicValue.ZERO) {
				return LogicValue.ZERO;
			}
			if (lv == LogicValue.X) {
				xthere = true;
			}
		}
		return xthere ? LogicValue.X : LogicValue.ONE;
	}

	public static LogicValue or(LogicValue l1, LogicValue l2) {
		if (l1 == null || l2 == null) {
			return null;
		} else {
			if (l1 == LogicValue.ONE || l2 == LogicValue.ONE) {
				return LogicValue.ONE;
			} else if (l1 == LogicValue.ZERO && l2 == LogicValue.ZERO) {
				return LogicValue.ZERO;
			} else {
				return LogicValue.X;
			}
		}
	}

	public static LogicValue or(List<LogicValue> ls) {
		boolean xthere = false;
		for (LogicValue lv : ls) {
			if (lv == LogicValue.ONE) {
				return LogicValue.ONE;
			}
			if (lv == null) {
				return null;
			}
			if (lv == LogicValue.X) {
				xthere = true;
			}
		}
		return xthere ? LogicValue.X : LogicValue.ZERO;
	}

	public static LogicValue nand(LogicValue l1, LogicValue l2) {
		return not(and(l1, l2));
	}

	public static LogicValue nand(List<LogicValue> ls) {
		return not(and(ls));
	}

	public static LogicValue nor(LogicValue l1, LogicValue l2) {
		return not(or(l1, l2));
	}

	public static LogicValue nor(List<LogicValue> ls) {
		return not(or(ls));
	}

	public static LogicValue xor(LogicValue l1, LogicValue l2) {
		if (l1 == null || l2 == null) {
			return null;
		} else {
			if (l1 == LogicValue.X || l2 == LogicValue.X) {
				return LogicValue.X;
			} else {
				return l1 != l2 ? LogicValue.ONE : LogicValue.ZERO;
			}
		}
	}

	public static LogicValue xor(List<LogicValue> ls) {
		int ones = 0;
		for (LogicValue lv : ls) {
			if (lv == LogicValue.ONE) {
				ones++;
			}
			if (lv == LogicValue.X) {
				return LogicValue.X;
			}
			if (lv == null) {
				return null;
			}
		}
		return (ones % 2) == 0 ? LogicValue.ZERO : LogicValue.ONE;
	}
}
