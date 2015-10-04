package org.pnwg.tools.diff.handler;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pnwg.tools.diff.context.ContextUtil;
import org.pnwg.tools.diff.context.IContext;
import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.diff.model.DiffType;
import org.pnwg.tools.helpers.FieldProcessor;
import org.pnwg.tools.helpers.FieldUtil;
import org.pnwg.tools.helpers.Pair;

public class CollectionHandler implements ITypeHandler<AbstractCollection<?>> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void isEqual(AbstractCollection<?> expected, AbstractCollection<?> actual, IContext context) {

		if (expected.size() != actual.size()) {
			// Create
			FieldProcessor.onDifference(expected.size(), actual.size(), null, DiffType.COLLECTION_SIZE_MISMATCH,
					context);
			// continue.
		}

		// if( ) sort collections by KeyFields
		List<Object> expectedColl = new ArrayList<>(expected);
		// Sample object to determine content type
		Object sampleObj = expectedColl.get(0);
		List<Object> actualColl = new ArrayList<>(actual);
		List<String> fields = null;
		if (context.config().getKeyFields(sampleObj.getClass()) != null) {
			fields = new ArrayList(context.config().getKeyFields(sampleObj.getClass()));
		}

		Map<String, List<Object>> expectedGroupedColl = groupCollectionByKey(expectedColl, fields);
		Map<String, List<Object>> actualGroupedColl = groupCollectionByKey(actualColl, fields);

		compareGroupedCollection(expectedGroupedColl, actualGroupedColl, context);

		/*
		 * if (fields != null && fields.size() != 0) { FieldsComparator cmp =
		 * new FieldsComparator(fields); Collections.sort(expectedColl, cmp);
		 * Collections.sort(actualColl, cmp); }
		 */

	}

	private void compareGroupedCollection(Map<String, List<Object>> expected, Map<String, List<Object>> actual,
			IContext context) {
		for (Map.Entry<String, List<Object>> expEntry : expected.entrySet()) {
			String key = expEntry.getKey();
			List<Object> expectedList = actual.get(key);
			List<Object> actualList = actual.get(key);

			if (actualList == null) {
				Diff diff = FieldProcessor.onDifference(expectedList, actualList, null,
						DiffType.COLLECTION_BY_KEY_ACTUAL_COLL_NULL, context);
				// set the key
				diff.setKey(key);
			} else if (expectedList.size() != actualList.size()) {
				Diff diff = FieldProcessor.onDifference(expectedList, actualList, null,
						DiffType.COLLECTION_BY_KEY_COLL_SIZE_MISMATCH, context);
				// set the key
				diff.setKey(key);
			} else {
				compareIdenticalKeyItemList(expectedList, actualList, context, key);
			}
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
				context.getDifferences().addAll(diffs);
			}
			expectedDup.remove(expItem);

			if (actualDup.size() == 0) {
				break;
			}
		}

		// At this point actualDup list has all unexpected items
		for (Object item : actualDup) {
			Diff diff = FieldProcessor.onDifference(null, item, null, DiffType.COLLECTION_UNEXPECTED_ITEM, context);
			diff.setKey(key);
		}

		// And expectedDup list has all missing items
		for (Object item : expectedDup) {
			Diff diff = FieldProcessor.onDifference(item, null, null, DiffType.COLLECTION_MISSING_ITEM, context);
			diff.setKey(key);
		}
	}

	/**
	 * Find the obj in the given list that has minimum differences. and removes
	 * the item from the list.
	 * 
	 * @param obj
	 * @param list
	 * @param context
	 * @return
	 */
	private List<Diff> findClosestItemDiffs(Object obj, List<Object> list, IContext context) {
		List<Diff> minDiffs = null;
		Map<Pair, List<Diff>> map = new HashMap<>();
		for (Object item : list) {
			IContext ctx = ContextUtil.copy(context);
			Pair pair = new Pair(obj, item);
			ctx.config().getFieldVisitor().visit(obj, item, ctx);
			map.put(pair, ctx.getDifferences());
		}

		for (Map.Entry<Pair, List<Diff>> entry : map.entrySet()) {
			if (minDiffs == null) {
				minDiffs = entry.getValue();
			}

			if (minDiffs.size() < entry.getValue().size()) {
				minDiffs = entry.getValue();
			}
		}

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

	public static class FieldsComparator implements Comparator<Object> {

		private List<String> fields;

		public FieldsComparator(List<String> fields) {
			this.fields = fields;
		}

		@Override
		public int compare(Object o1, Object o2) {
			if (o1 == null) {
				return -1;
			}
			if (o2 == null) {
				return 1;
			}

			String o1Str = FieldUtil.buildKey(o1, fields);
			String o2Str = FieldUtil.buildKey(o2, fields);
			return o1Str.compareTo(o2Str);
		}

	}

}
