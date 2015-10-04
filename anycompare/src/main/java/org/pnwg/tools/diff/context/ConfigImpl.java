package org.pnwg.tools.diff.context;

import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pnwg.tools.diff.handler.CollectionHandler;
import org.pnwg.tools.diff.handler.ITypeHandler;
import org.pnwg.tools.diff.listener.IDifferenceListner;
import org.pnwg.tools.diff.listener.LoggingDifferenceListener;
import org.pnwg.tools.helpers.FieldProcessor;
import org.pnwg.tools.helpers.FieldUtil;
import org.pnwg.tools.helpers.FieldUtil.FieldNamePart;
import org.pnwg.tools.helpers.FieldVisitor;
import org.pnwg.tools.helpers.IFieldProcessor;
import org.pnwg.tools.helpers.IFieldVisitor;

public class ConfigImpl implements IConfig {

	private Map<String, ITypeHandler<?>> typeHandlers = new HashMap<>();
	private Set<String> ignoreFields = new HashSet<>();
	private List<IDifferenceListner> diffListeners = new LinkedList<>();
	/**
	 * Key fields organized by class names.
	 */
	private Map<String, Set<String>> keyFields = new HashMap<>();

	private Set<String> baseClasses = new HashSet<>();

	private IFieldVisitor fieldVisitor = new FieldVisitor();

	private IFieldProcessor fieldProcessor = new FieldProcessor();

	public ConfigImpl() {
		initializeDefaults();
	}

	@SuppressWarnings("unchecked")
	protected IConfig initializeDefaults() {
		register(new FieldVisitor());
		register(new FieldProcessor());
		register(new LoggingDifferenceListener());
		@SuppressWarnings("rawtypes")
		ITypeHandler collectionHandler = new CollectionHandler();
		register(AbstractCollection.class, collectionHandler);
		return this;
	}

	@Override
	public <T> IConfig register(Class<T> clazz, ITypeHandler<T> handler) {
		typeHandlers.put(clazz.getName(), handler);
		return this;
	}

	@Override
	public IConfig register(IDifferenceListner listener) {
		diffListeners.add(listener);
		return this;
	}

	@Override
	public IConfig register(FieldFeature feature, String... fullyQualifiedField) {
		register(feature, Arrays.asList(fullyQualifiedField));
		return this;
	}

	@Override
	public IConfig register(FieldFeature feature, List<String> fullyQualifiedField) {
		switch (feature) {
		case IGNORE_FIELD:
			ignoreFields.addAll(fullyQualifiedField);
			break;
		case KEY_FIELD:
			for (String fqnField : fullyQualifiedField) {
				String key = FieldUtil.getFieldNamePart(fqnField, FieldNamePart.CLASS);
				String fieldName = FieldUtil.getFieldNamePart(fqnField, FieldNamePart.FIELD_NAME);
				if (keyFields.get(key) == null) {
					keyFields.put(key, new HashSet<String>());
				}
				keyFields.get(key).add(fieldName);
			}

			break;
		}
		return this;
	}

	@Override
	public IConfig registerBaseClass(Class<?> clazz) {
		baseClasses.add(clazz.getName());
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> ITypeHandler<T> getTypeHandler(Class<T> clazz) {
		ITypeHandler<T> handler = null;
		Class<?> currClass = clazz;
		while (currClass != null) {
			String cname = currClass.getName();
			handler = (ITypeHandler<T>) typeHandlers.get(cname);

			// Optimization: if we already remember to have this in our handler
			// collection, return.
			if (typeHandlers.containsKey(cname)) {
				break;
			}

			// If we found a handler
			if (handler != null) {
				break;
			}
			// If we dont have a handler yet,
			// try if there is one for super class
			currClass = currClass.getSuperclass();

			// Optimization: remember that we didnt find one for this class
			typeHandlers.put(cname, null);
		}
		return handler;
	}

	@Override
	public boolean isRegisteredBaseClass(Class<?> clazz) {
		return baseClasses.contains(clazz.getName());
	}

	@Override
	public IConfig register(IFieldVisitor fieldVisitor) {
		this.fieldVisitor = fieldVisitor;
		return this;
	}

	@Override
	public IConfig register(IFieldProcessor fieldprocessor) {
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
	public Set<String> getKeyFields(Class<?> clazz) {
		return keyFields.get(clazz.getName());
	}

	@Override
	public List<IDifferenceListner> getDifferenceListners() {
		return diffListeners;
	}
}
