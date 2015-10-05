package org.pnwg.tools.helpers;

import java.lang.reflect.Field;

import org.pnwg.tools.diff.context.IContext;
import org.pnwg.tools.diff.handler.ITypeHandler;
import org.pnwg.tools.diff.handler.SimpleTypeHandler;
import org.pnwg.tools.diff.model.DiffType;

public class FieldProcessor implements IFieldProcessor {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void process(Object expectedObj, Object actualObj, Field field, IContext context) {

		// Check if field is ignorable
		if (context.config().isIgnoreField(field)) {
			return;
		}

		if (expectedObj != null && actualObj == null) {
			context.onDifference(expectedObj, actualObj, field, DiffType.ACTUAL_OBJECT_NULL);
			return;
		}

		if (actualObj != null && expectedObj == null) {
			context.onDifference(expectedObj, actualObj, field, DiffType.EXPECTED_OBJECT_NULL);
			return;
		}

		Object expected = FieldUtil.getField(expectedObj, field);
		Object actual = FieldUtil.getField(actualObj, field);

		// If objects are plainly equal.
		if (expected == actual) {
			return;
		}

		// If we happen to compare this pair already
		Pair pair = new Pair(expected, actual);
		if (context.checkPairRegistery(pair)) {
			// return;
		}

		context.addPairToRegistry(pair);

		Class<?> expectedClazz = expected.getClass();

		// Primitive check
		if (expectedClazz.isPrimitive() && expected != actual) {
			context.onDifference(expectedObj, actualObj, field, DiffType.VALUE_MISMATCH);
			return;
		}

		ITypeHandler handler = context.config().getTypeHandler(expectedClazz);
		if (handler != null) {
			// SimpleTypeHandler is expected to be User defined handler
			if (handler instanceof SimpleTypeHandler) {
				boolean isEqual = ((SimpleTypeHandler) handler).isEqual(expected, actual);
				if (!isEqual) {
					context.onDifference(expectedObj, actualObj, field, DiffType.CUSTOM_COMPARE);
					return;
				}
			} else {
				// this is expected to be detailed handler that
				// modifies the context with diffs
				handler.isEqual(expected, actual, context);
				return;
			}
		}

		if (!expected.equals(actual)) {
			// If we are here that means these objects are not equal
			context.onDifference(expectedObj, actualObj, field, DiffType.VALUE_MISMATCH);
		}
	}
}
