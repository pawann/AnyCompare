package org.pnwg.tools.helpers;

import org.pnwg.tools.diff.context.IContext;

public interface IFieldVisitor {

	/**
	 * Visit each field in the object and call {@link IFieldProcessor} for each
	 * field.
	 * 
	 * @param expected
	 * @param actual
	 * @param processor
	 */
	void visit(Object expected, Object actual, IContext context);
}
