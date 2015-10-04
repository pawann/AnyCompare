package org.pnwg.tools.helpers;

import java.lang.reflect.Field;

import org.pnwg.tools.diff.context.IContext;

/**
 * 
 * @author Pawan
 *
 */
public class FieldVisitor implements IFieldVisitor {

	/**
	 * Visit each field in the expected and actual object, and call the
	 * processor.
	 */
	@Override
	public void visit(Object expected, Object actual, IContext context) {
		IFieldProcessor processor = context.config().getFieldProcessor();
		if (expected == null || actual == null) {
			processor.process(expected, actual, null, context);
		}

		Class<?> expClazz = expected.getClass();

		while (expClazz != null) {
			Field fields[] = expClazz.getDeclaredFields();
			for (Field field : fields) {
				processor.process(expected, actual, field, context);
			}

			// If current class is registered as the last base class to be
			// checked, Breakout.
			if (context.config().isRegisteredBaseClass(expClazz)) {
				break;
			}

			expClazz = expClazz.getSuperclass();
		}

	}

}
