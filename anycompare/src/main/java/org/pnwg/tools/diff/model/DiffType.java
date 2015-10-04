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
	 * If collections are not of the same size
	 */
	COLLECTION_SIZE_MISMATCH,

	/**
	 * If expected collection is null, but actual item has it
	 */
	COLLECTION_UNEXPECTED,

	/**
	 * Unexpected item in the actual collection
	 */
	COLLECTION_UNEXPECTED_ITEM,

	/**
	 * Missing item in the actual collection
	 */
	COLLECTION_MISSING_ITEM,

	/**
	 * Collection for a key do not match
	 */
	COLLECTION_BY_KEY_ACTUAL_COLL_NULL,

	/**
	 * Collection for a key do not match
	 */
	COLLECTION_BY_KEY_ACTUAL_COLL_UNEXPECTED,

	/**
	 * Number of items for a key do not match
	 */
	COLLECTION_BY_KEY_COLL_SIZE_MISMATCH;
}
