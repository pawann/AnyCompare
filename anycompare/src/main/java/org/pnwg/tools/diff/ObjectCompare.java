package org.pnwg.tools.diff;

import org.pnwg.tools.diff.context.ContextUtil;
import org.pnwg.tools.diff.context.IContext;

/**
 * 
 * @author Pawan
 *
 */
public class ObjectCompare {

	/**
	 * Tells if 2 objects are equal or not using default context
	 * {@link IContext#hasDifferences()} can be used to determine the
	 * differences.
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	public static IContext compare(Object expected, Object actual) {
		return compare(expected, actual, buildContext());
	}

	/**
	 * Tells if 2 objects are equal or not using provided context
	 * {@link IContext#hasDifferences()} can be used to determine the
	 * differences.
	 * 
	 * @param expected
	 * @param actual
	 * @param context
	 * @return
	 */
	public static IContext compare(Object expected, Object actual, IContext context) {
		context.config().getFieldVisitor().visit(expected, actual, context);
		return context;
	}

	/**
	 * Builds a fresh {@link IContext}
	 * 
	 * @return
	 */
	public static IContext buildContext() {
		return ContextUtil.newContext();
	}

}
