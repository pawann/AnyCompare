package org.pnwg.tools.diff.model;

import org.pnwg.tools.diff.handler.SimpleTypeHandler;

public enum DiffType {

	/**
	 * Literal values don't match
	 */
	VALUE_MISMATCH,

	/**
	 * Expected object is null
	 */
	EXPECTED_OBJECT_NULL,

	/**
	 * Actual object is null
	 */
	ACTUAL_OBJECT_NULL,

	/**
	 * Difference determined by custom {@link SimpleTypeHandler}
	 */
	CUSTOM_COMPARE,

	/**
	 * This diff was ignored even before comparison
	 */
	IGNORED;

}
