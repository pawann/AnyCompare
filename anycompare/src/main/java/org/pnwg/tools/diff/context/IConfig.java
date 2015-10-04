package org.pnwg.tools.diff.context;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.pnwg.tools.diff.handler.ITypeHandler;
import org.pnwg.tools.diff.listener.IDifferenceListner;
import org.pnwg.tools.helpers.IFieldProcessor;
import org.pnwg.tools.helpers.IFieldVisitor;

public interface IConfig {

	/**
	 * Registers a Type handler for comparison
	 * 
	 * @param handler
	 * @return
	 */
	<T> IConfig register(Class<T> clazz, ITypeHandler<T> handler);

	/**
	 * Register a custom DifferenceListner Calling this method multiple times
	 * registers multiple listeners.
	 * 
	 * @param listener
	 * @return
	 */
	IConfig register(IDifferenceListner listener);

	/**
	 * Register a fully qualified field name for a feature. fullyQualifiedField
	 * name is of the form "com.example.test.Person.age"
	 * 
	 * @param feature
	 * @param fullyQualifiedField
	 * @return
	 */
	IConfig register(FieldFeature feature, String... fullyQualifiedField);

	/**
	 * Register List of fully qualified field names for a feature.
	 * fullyQualifiedField name is of the form "com.example.test.Person.age"
	 * 
	 * @param feature
	 * @param fullyQualifiedField
	 * @return
	 */
	IConfig register(FieldFeature feature, List<String> fullyQualifiedField);

	/**
	 * Register field visitor
	 * 
	 * @param fieldVisitor
	 * @return
	 */
	IConfig register(IFieldVisitor fieldVisitor);

	/**
	 * Register field processor
	 * 
	 * @param fieldprocessor
	 * @return
	 */
	IConfig register(IFieldProcessor fieldprocessor);

	/**
	 * Register base class after which comparison should stop and fields from
	 * super type should not be compared. Calling this method multiple times
	 * registers multiple classes as base class.
	 * 
	 * @param clazz
	 * @return
	 */
	IConfig registerBaseClass(Class<?> clazz);

	/**
	 * 
	 * @param field
	 * @return
	 */
	boolean isIgnoreField(Field field);

	/**
	 * Get keyFields for current class
	 * 
	 * @param field
	 * @return
	 */
	Set<String> getKeyFields(Class<?> clazz);

	/**
	 * Get registered comparison type handler for given class. If current class
	 * does not have a handler, super types are examined to see if they have a
	 * handler.
	 * 
	 * @param clazz
	 * @return
	 */
	<T> ITypeHandler<T> getTypeHandler(Class<T> clazz);

	/**
	 * Check if provided class is a registered base class.
	 * 
	 * @return
	 */
	boolean isRegisteredBaseClass(Class<?> clazz);

	/**
	 * Get current field visitor
	 * 
	 * @return
	 */
	IFieldVisitor getFieldVisitor();

	/**
	 * Get current field processor
	 * 
	 * @return
	 */
	IFieldProcessor getFieldProcessor();

	/**
	 * Get registered difference listeners
	 * 
	 * @return
	 */
	List<IDifferenceListner> getDifferenceListners();

}
