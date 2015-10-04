package org.pnwg.tools.helpers;

import java.util.Objects;

public class Pair {

	private final Object expected;
	private final Object actual;

	public Pair(Object expected, Object actual) {
		this.expected = expected;
		this.actual = actual;
	}

	public Object getExpected() {
		return expected;
	}

	public Object getActual() {
		return actual;
	}

	@Override
	public boolean equals(Object obj) {
		Pair pair = (Pair) obj;
		return equalsToOther(expected, pair.getExpected()) && equalsToOther(actual, pair.getActual());
	}

	@Override
	public int hashCode() {
		return Objects.hash(expected, actual);
	}

	private boolean equalsToOther(Object obj1, Object obj2) {
		boolean isEquals = false;
		if ((obj1 == null && obj2 == null) || (obj1 != null && obj1.equals(obj2))
				|| (obj2 != null && obj2.equals(obj1))) {
			isEquals = true;
		}
		return isEquals;
	}
}
