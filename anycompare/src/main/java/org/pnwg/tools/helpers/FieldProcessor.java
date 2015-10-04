package org.pnwg.tools.helpers;

import java.lang.reflect.Field;

import org.pnwg.tools.diff.context.IContext;
import org.pnwg.tools.diff.handler.ITypeHandler;
import org.pnwg.tools.diff.handler.SimpleTypeHandler;
import org.pnwg.tools.diff.listener.IDifferenceListner;
import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.diff.model.DiffType;

public class FieldProcessor implements IFieldProcessor {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void process(Object expectedObj, Object actualObj, Field field, IContext context) {
		// Check if field is ignorable
		if (context.isIgnoreField(field)) {
			onDifference(expectedObj, actualObj, field, DiffType.IGNORED, context);
			return;
		}

		if (expectedObj != null && actualObj == null) {
			onDifference(expectedObj, actualObj, field, DiffType.ACTUAL_OBJECT_NULL, context);
			return;
		}

		if (actualObj != null && expectedObj == null) {
			onDifference(expectedObj, actualObj, field, DiffType.EXPECTED_OBJECT_NULL, context);
			return;
		}

		Object expected = FieldUtil.getField(expectedObj, field);
		Object actual = FieldUtil.getField(actualObj, field);

		// If objects are plainly equal.
		if (expected == actual || expected.equals(actual)) {
			return;
		}

		// If we happen to compare this pair already
		Pair pair = new Pair(expected, actual);
		if (context.checkPairRegistery(pair)) {
			return;
		}

		context.addPairToRegistry(pair);

		Class<?> expectedClazz = expected.getClass();

		// Primitive check
		if (expectedClazz.isPrimitive() && expected != actual) {
			onDifference(expectedObj, actualObj, field, DiffType.VALUE_MISMATCH, context);
			return;
		}

		if (context.getTypeHandler(expectedClazz) != null) {
			ITypeHandler handler = context.getTypeHandler(expectedClazz);
			// SimpleTypeHandler is expected to be User defined handler
			if (handler instanceof SimpleTypeHandler) {
				boolean isEqual = ((SimpleTypeHandler) handler).isEqual(expected, actual);
				if (!isEqual) {
					onDifference(expectedObj, actualObj, field, DiffType.CUSTOM_COMPARE, context);
					return;
				}
			} else {
				// this is expected to be detailed handler that
				// modifies the context with diffs
				handler.isEqual(expected, actual, context);
				return;
			}
		}

		// If we are here that means these objects are not equal
		onDifference(expectedObj, actualObj, field, DiffType.VALUE_MISMATCH, context);
	}

	public static void onDifference(Object expectedObj, Object actualObj, Field field, DiffType type,
			IContext context) {
		Diff diff = new Diff(expectedObj, actualObj, field, type);
		context.addDiff(diff);
		for (IDifferenceListner listener : context.getDifferenceListners()) {
			listener.onDifference(diff);
		}
	}

}
