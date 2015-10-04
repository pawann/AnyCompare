package org.pnwg.tools.diff.model;

import java.lang.reflect.Field;

/**
 * 
 * @author Pawan
 *
 */
public class Diff {

	/**
	 * Reference to expected object
	 */
	private Object expected;

	/**
	 * Reference to actual object
	 */
	private Object actual;

	/**
	 * Field that has caused the difference, this could be <code>null</code> for
	 * literal differences.
	 */
	private Field field;

	/**
	 * Difference type
	 */
	private DiffType type;

	public Object getExpected() {
		return expected;
	}

	public void setExpected(Object expected) {
		this.expected = expected;
	}

	public Object getActual() {
		return actual;
	}

	public void setActual(Object actual) {
		this.actual = actual;
	}

	public Object getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public DiffType getType() {
		return type;
	}

	public void setType(DiffType type) {
		this.type = type;
	}

	public Object getExpectedValue() {
		return getFieldValue(expected, field);
	}

	public Object getActualValue() {
		return getFieldValue(actual, field);
	}

	public static Object getFieldValue(Object obj, Field field) {
		Object value = null;
		if (obj != null && field != null) {
			field.setAccessible(true);
			try {
				value = field.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// Not a problem, we'll simply return null
			}
		}
		return value;
	}

}
