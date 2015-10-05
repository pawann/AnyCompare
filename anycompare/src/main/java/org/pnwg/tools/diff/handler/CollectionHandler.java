package org.pnwg.tools.diff.handler;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pnwg.tools.diff.context.ContextUtil;
import org.pnwg.tools.diff.context.IContext;
import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.diff.model.DiffType;
import org.pnwg.tools.helpers.FieldUtil;
import org.pnwg.tools.helpers.Pair;

public class CollectionHandler implements ITypeHandler<AbstractCollection<?>> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void isEqual(AbstractCollection<?> expected, AbstractCollection<?> actual, IContext context) {

		// If expected is null or empty
		if (expected == null || expected.size() == 0) {
			if (actual != null && actual.size() != 0) {
				context.onDifference(null, actual, null, DiffType.COLLECTION_UNEXPECTED_ITEM);
			}
			return;
		}

		if (expected.size() != actual.size()) {
			// Create
			context.onDifference(expected.size(), actual.size(), null, DiffType.COLLECTION_SIZE_MISMATCH);
			// continue.
		}

		// if( ) sort collections by KeyFields
		List<Object> expectedColl = new ArrayList<>(expected);
		// Sample object to determine content type, find the first non-null one
		Object sampleObj = null;
		int index = 0;
		while (sampleObj == null || index < expectedColl.size()) {
			sampleObj = expectedColl.get(index++);
		}

		List<Object> actualColl = new ArrayList<>(actual);
		List<String> fields = null;
		if (context.config().getKeyFields(sampleObj.getClass()) != null) {
			fields = new ArrayList(context.config().getKeyFields(sampleObj.getClass()));
		}

		Map<String, List<Object>> expectedGroupedColl = groupCollectionByKey(expectedColl, fields);
		Map<String, List<Object>> actualGroupedColl = groupCollectionByKey(actualColl, fields);

		compareGroupedCollection(expectedGroupedColl, actualGroupedColl, context);
	}

	private void compareGroupedCollection(Map<String, List<Object>> expected, Map<String, List<Object>> actual,
			IContext context) {
		for (Map.Entry<String, List<Object>> expEntry : expected.entrySet()) {
			String key = expEntry.getKey();
			List<Object> expectedList = expected.get(key);
			List<Object> actualList = actual.get(key);

			if (actualList == null) {
				Diff diff = context.onDifference(expectedList, actualList, null,
						DiffType.COLLECTION_BY_KEY_ACTUAL_COLL_NULL);
				// set the key
				diff.setKey(key);
			} else if (expectedList.size() != actualList.size()) {
				Diff diff = context.onDifference(expectedList, actualList, null,
						DiffType.COLLECTION_BY_KEY_COLL_SIZE_MISMATCH);
				// set the key
				diff.setKey(key);
			}

			compareIdenticalKeyItemList(expectedList, actualList, context, key);

		}
	}

	/**
	 * Compare two lists that have items with same key. for each expectedItem,
	 * find closest match in actual list and mark the pair as compared. Move on
	 * to next item. At the end we'll be left with expected and missing items in
	 * the dup collections.
	 * 
	 * @param expected
	 * @param actual
	 */
	private void compareIdenticalKeyItemList(List<Object> expected, List<Object> actual, IContext context, String key) {
		// Duplicate the list
		List<Object> actualDup = new ArrayList<>(actual);
		List<Object> expectedDup = new ArrayList<>(expected);
		for (Object expItem : expected) {
			List<Diff> diffs = findClosestItemDiffs(expItem, actualDup, context);
			if (diffs != null && !diffs.isEmpty()) {
				context.addDiff(diffs);
			}
			expectedDup.remove(expItem);

			if (actualDup.size() == 0) {
				break;
			}
		}

		// At this point actualDup list has all unexpected items
		for (Object item : actualDup) {
			Diff diff = context.onDifference(null, item, null, DiffType.COLLECTION_UNEXPECTED_ITEM);
			diff.setKey(key);
		}

		// And expectedDup list has all missing items
		for (Object item : expectedDup) {
			Diff diff = context.onDifference(item, null, null, DiffType.COLLECTION_MISSING_ITEM);
			diff.setKey(key);
		}
	}

	/**
	 * Find the obj in the given list that has minimum differences and remove
	 * that item from the list being serached.
	 * 
	 * @param obj
	 * @param list
	 * @param context
	 * @return
	 */
	private List<Diff> findClosestItemDiffs(Object obj, List<Object> list, IContext context) {
		List<Diff> minDiffs = null;
		Map<Pair, List<Diff>> map = new HashMap<>();
		Object foundObj = null;
		for (Object item : list) {
			IContext ctx = ContextUtil.copy(context);
			Pair pair = new Pair(obj, item);
			// We dont want to call listeners while trying to find closest
			// match.
			ctx.enableListeners(false);
			ctx.config().getFieldVisitor().visit(obj, item, ctx);
			if (!ctx.hasDifferences()) {
				// current item is exact match
				foundObj = item;
				minDiffs = ctx.getDifferences();
				break;
			}

			map.put(pair, ctx.getDifferences());
		}

		if (foundObj == null) {
			for (Map.Entry<Pair, List<Diff>> entry : map.entrySet()) {
				if (minDiffs == null) {
					minDiffs = entry.getValue();
					foundObj = entry.getKey().getActual();
				}

				if (minDiffs.size() < entry.getValue().size()) {
					minDiffs = entry.getValue();
					foundObj = entry.getKey().getActual();
				}
			}
		}

		list.remove(foundObj);

		return minDiffs;
	}

	/**
	 * Given a list of Items, and a list of key fields. Organize the items in
	 * buckets of keys.
	 * 
	 * @param list
	 * @param fields
	 * @return
	 */
	private Map<String, List<Object>> groupCollectionByKey(List<Object> list, List<String> fields) {
		Map<String, List<Object>> map = new HashMap<>();
		for (Object item : list) {
			String key = FieldUtil.buildKey(item, fields);
			if (map.get(key) == null) {
				map.put(key, new ArrayList<>());
			}
			map.get(key).add(item);
		}
		return map;
	}
}
