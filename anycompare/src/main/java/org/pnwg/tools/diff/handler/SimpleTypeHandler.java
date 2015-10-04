package org.pnwg.tools.diff.handler;

import org.pnwg.tools.diff.context.IContext;

public abstract class SimpleTypeHandler<T> implements ITypeHandler<T> {

	/**
	 * To override this method implement {@link ITypeHandler}
	 */
	@Override
	public final void isEqual(T expected, T actual, IContext context) {
		return;
	}

	/**
	 * Method to be extended to customize the comparison for type T.
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	public abstract boolean isEqual(T expected, T actual);

}
