package org.pnwg.tools.diff.context;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pnwg.tools.diff.handler.ITypeHandler;
import org.pnwg.tools.diff.listener.IDifferenceListner;
import org.pnwg.tools.diff.model.Diff;

public class CompareContextImpl implements IContext {

	private Map<String, ITypeHandler<?>> typeHandlers = new HashMap<>();

	private List<IDifferenceListner> diffListeners = new LinkedList<>();

	private Set<String> ignoreFields = new HashSet<>();

	private Set<String> keyFields = new HashSet<>();

	private Set<String> baseClasses = new HashSet<>();

	private List<Diff> diffs = new LinkedList<>();

	@Override
	public <T> IContext register(Class<T> clazz, ITypeHandler<T> handler) {
		typeHandlers.put(clazz.getName(), handler);
		return this;
	}

	@Override
	public IContext register(IDifferenceListner listener) {
		diffListeners.add(listener);
		return this;
	}

	@Override
	public IContext register(FieldFeature feature, String... fullyQualifiedField) {
		register(feature, Arrays.asList(fullyQualifiedField));
		return this;
	}

	@Override
	public IContext register(FieldFeature feature, List<String> fullyQualifiedField) {
		Collection<String> coll = null;
		switch (feature) {
		case IGNORE_FIELD:
			coll = ignoreFields;
			break;
		case KEY_FIELD:
			coll = keyFields;
			break;
		}
		if (coll != null) {
			coll.addAll(fullyQualifiedField);
		}
		return this;
	}

	@Override
	public IContext registerBaseClass(Class<?> clazz) {
		baseClasses.add(clazz.getName());
		return this;
	}

	@Override
	public List<Diff> getDifferences() {
		return diffs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ITypeHandler<T> getTypeHandler(Class<T> clazz) {
		String cname = clazz.getName();
		return (ITypeHandler<T>) typeHandlers.get(cname);
	}

	@Override
	public List<IDifferenceListner> getDifferenceListners() {
		return diffListeners;
	}

	@Override
	public void addDiff(Diff diff) {
		diffs.add(diff);
	}
}
