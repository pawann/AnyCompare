package org.pnwg.tools.helpers;

import java.lang.reflect.Field;
import java.util.List;

public class FieldUtil {

	private static final String FIELD_NAME_SEPARATOR = ".";

	private static final String KEY_SEPARATOR = "|";

	/**
	 * Return a String of the form com.example.test.Person.age, where age is
	 * represented by Field object.
	 * 
	 * @param field
	 * @return
	 */
	public static String makeFieldName(Field field) {
		String fqnFname = "";
		if (field != null) {
			String cname = field.getDeclaringClass().getName();
			String fname = field.getName();
			fqnFname = cname + FIELD_NAME_SEPARATOR + fname;
		}
		return fqnFname;
	}

	/**
	 * Retrieve the field name or class name from below format:
	 * 
	 * com.example.test.Person.age class part will be "com.example.test.Person"
	 * and fieldName part will be "age"
	 * 
	 * @param fullyQualifiedFieldName
	 * @param fieldNamePart
	 * @return
	 */
	public static String getFieldNamePart(String fullyQualifiedFieldName, FieldNamePart fieldNamePart) {
		int lastSeparatorIndex = fullyQualifiedFieldName.lastIndexOf(FIELD_NAME_SEPARATOR);
		String fieldName = fullyQualifiedFieldName.substring(lastSeparatorIndex + 1);
		String className = fullyQualifiedFieldName.substring(0, lastSeparatorIndex);
		String value = FieldNamePart.CLASS.equals(fieldNamePart) ? className : fieldName;
		return value;
	}

	/**
	 * Retrieve value of a field from given object.
	 * 
	 * @param obj
	 * @param field
	 * @return
	 */
	public static Object getField(Object obj, Field field) {
		Object valField = null;
		if (obj != null && field != null) {
			field.setAccessible(true);
			try {
				valField = field.get(obj);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// Do nothing
			}
		}
		return valField;
	}

	/**
	 * Retrieve value of a field from given object.
	 * 
	 * @param obj
	 * @param field
	 * @return
	 */
	public static Object getField(Object obj, String fieldName) {
		Object valField = null;
		Field field = null;
		if (obj != null && fieldName != null) {
			field = findField(obj, fieldName);
			valField = getField(obj, field);
		}
		return valField;
	}

	/**
	 * Find a field in given object
	 * 
	 * @param obj
	 * @param fieldName
	 * @return
	 */
	public static Field findField(Object obj, String fieldName) {
		Class<?> clazz = obj.getClass();
		Field field = null;
		while (clazz != null && field == null) {
			try {
				field = clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException | SecurityException e) {
				// Do nothing
			}

			clazz = clazz.getSuperclass();
		}

		return field;
	}

	/**
	 * Build a key using simply concatenating the values for fields.
	 * 
	 * @param obj
	 * @param fields
	 * @return
	 */
	public static String buildKey(Object obj, List<String> fields) {
		StringBuilder key = new StringBuilder();
		for (String field : fields) {
			Object val = getField(obj, field);
			String valStr = val != null ? val.toString() : "";
			key.append(field).append("=").append(valStr).append(KEY_SEPARATOR);
		}
		return key.toString();
	}

	public static enum FieldNamePart {
		CLASS, FIELD_NAME;
	}
}
