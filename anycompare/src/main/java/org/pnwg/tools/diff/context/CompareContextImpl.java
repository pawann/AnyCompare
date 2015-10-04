package org.pnwg.tools.diff.context;

import java.lang.reflect.Field;
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
import org.pnwg.tools.diff.listener.LoggingDifferenceListener;
import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.diff.model.DiffType;
import org.pnwg.tools.helpers.FieldProcessor;
import org.pnwg.tools.helpers.FieldUtil;
import org.pnwg.tools.helpers.FieldVisitor;
import org.pnwg.tools.helpers.IFieldProcessor;
import org.pnwg.tools.helpers.IFieldVisitor;
import org.pnwg.tools.helpers.Pair;

public class CompareContextImpl implements IContext {

	private Map<String, ITypeHandler<?>> typeHandlers = new HashMap<>();

	private List<IDifferenceListner> diffListeners = new LinkedList<>();

	private Set<String> ignoreFields = new HashSet<>();

	private Set<String> keyFields = new HashSet<>();

	private Set<String> baseClasses = new HashSet<>();

	private List<Diff> diffs = new LinkedList<>();

	private IFieldVisitor fieldVisitor = new FieldVisitor();

	private IFieldProcessor fieldProcessor = new FieldProcessor();

	private Set<Pair> pairRegistry = new HashSet<>();

	private boolean foundDiffs = false;

	public CompareContextImpl() {
		initializeDefaults();
	}

	protected IContext initializeDefaults() {
		register(new FieldVisitor());
		register(new FieldProcessor());
		register(new LoggingDifferenceListener());
		return this;
	}

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
		if (diff.getType() != DiffType.IGNORED) {
			foundDiffs = true;
		}
		diffs.add(diff);
	}

	@Override
	public boolean isRegisteredBaseClass(Class<?> clazz) {
		return baseClasses.contains(clazz.getName());
	}

	@Override
	public IContext register(IFieldVisitor fieldVisitor) {
		this.fieldVisitor = fieldVisitor;
		return this;
	}

	@Override
	public IContext register(IFieldProcessor fieldprocessor) {
		this.fieldProcessor = fieldprocessor;
		return this;
	}

	@Override
	public IFieldVisitor getFieldVisitor() {
		return fieldVisitor;
	}

	@Override
	public IFieldProcessor getFieldProcessor() {
		return fieldProcessor;
	}

	@Override
	public boolean isIgnoreField(Field field) {
		return ignoreFields.contains(FieldUtil.makeFieldName(field));
	}

	@Override
	public boolean isKeyField(Field field) {
		return keyFields.contains(FieldUtil.makeFieldName(field));
	}

	@Override
	public void addPairToRegistry(Pair pair) {
		pairRegistry.add(pair);
	}

	@Override
	public boolean checkPairRegistery(Pair pair) {
		return pairRegistry.contains(pair);
	}

	@Override
	public boolean hasDifferences() {
		return foundDiffs;
	}

	@Override
	public void reset() {
		pairRegistry.clear();
		foundDiffs = false;
	}
}
