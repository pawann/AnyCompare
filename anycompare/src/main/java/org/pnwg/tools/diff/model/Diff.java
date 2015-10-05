package org.pnwg.tools.diff.model;

import java.lang.reflect.Field;

import org.pnwg.tools.helpers.FieldUtil;

/**
 * 
 * @author Pawan
 *
 */
public class Diff {

	public Diff() {
	}

	public Diff(Object expected, Object actual, Field field, DiffType type) {
		this.expected = expected;
		this.actual = actual;
		this.field = field;
		this.type = type;
	}

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

	/**
	 * Key - in case of collection diff.
	 */
	private String key;

	/**
	 * Current diff location
	 */
	private String diffLocation;

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

	public Field getField() {
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getExpectedValue() {
		if (field == null) {
			return expected;
		}
		return getFieldValue(expected, field);
	}

	public Object getActualValue() {
		if (field == null) {
			return actual;
		}

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

	@Override
	public String toString() {
		return "{ expected: " + getExpectedValue() + ", actual: " + getActualValue() + ", field: "
				+ FieldUtil.makeFieldName(field) + ", type:" + type + ", Key:" + key + " }";
	}

	public String getDiffLocation() {
		return diffLocation;
	}

	public void setDiffLocation(String diffLocation) {
		this.diffLocation = diffLocation;
	}

}
