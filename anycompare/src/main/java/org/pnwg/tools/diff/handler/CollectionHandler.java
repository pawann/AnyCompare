package org.pnwg.tools.diff.handler;

import java.util.Collection;

import org.pnwg.tools.diff.context.IContext;

public class CollectionHandler implements ITypeHandler<Collection<?>> {

	@Override
	public void isEqual(Collection<?> expected, Collection<?> actual, IContext context) {

		if (expected.size() != actual.size()) {
			// Create
			// FieldProcessor.onDifference(diff, context);
			// continue.
		}

		// if( ) sort collections by KeyFields

	}

}
