package org.pnwg.tools.diff.handler;

import org.pnwg.tools.diff.context.IContext;

public interface ITypeHandler<T> {

	/**
	 * Compare expected and actual object, return true if objects are equal else
	 * return false. Optionally context can be also be updated.
	 * 
	 * @param expected
	 * @param actual
	 * @param context
	 * @return
	 */
	void isEqual(T expected, T actual, IContext context);
}
