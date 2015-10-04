package org.pnwg.tools.diff.listener;

import java.lang.reflect.Field;

import org.pnwg.tools.diff.context.IContext;

public interface IDifferenceListner {

	boolean onDifference(Object expected, Object actual, Field field, IContext context);

}
