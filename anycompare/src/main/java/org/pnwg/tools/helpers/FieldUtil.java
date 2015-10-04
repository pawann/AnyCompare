package org.pnwg.tools.helpers;

import java.lang.reflect.Field;

public class FieldUtil {

	public static String makeFieldName(Field field) {
		String fqnFname = "";
		if (field != null) {
			String cname = field.getDeclaringClass().getName();
			String fname = field.getName();
			fqnFname = cname + "." + fname;
		}
		return fqnFname;
	}

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
}
