package org.pnwg.tools.diff.model;

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
	 * This diff was ignored even before comparison
	 */
	IGNORED;

}
