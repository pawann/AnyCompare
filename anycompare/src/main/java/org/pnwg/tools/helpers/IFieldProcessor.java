package org.pnwg.tools.helpers;

import java.lang.reflect.Field;

import org.pnwg.tools.diff.context.IContext;

/**
 * 
 * @author Pawan
 *
 */
public interface IFieldProcessor {

	void process(Object expected, Object actual, Field field, IContext context);
}
