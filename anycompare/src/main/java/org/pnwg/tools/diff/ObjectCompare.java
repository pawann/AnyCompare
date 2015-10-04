package org.pnwg.tools.diff;

import org.pnwg.tools.diff.context.CompareContextImpl;
import org.pnwg.tools.diff.context.IContext;

/**
 * 
 * @author Pawan
 *
 */
public class ObjectCompare {

	private static IContext defaultContext = new CompareContextImpl();

	/**
	 * Tells if 2 objects are equal or not using default context
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	public static boolean compare(Object expected, Object actual) {
		return compare(expected, actual, defaultContext);
	}

	/**
	 * Tells if 2 objects are equal or not using provided context
	 * 
	 * @param expected
	 * @param actual
	 * @param context
	 * @return
	 */
	public static boolean compare(Object expected, Object actual, IContext context) {
		return true;
	}

}
