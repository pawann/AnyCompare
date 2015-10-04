package org.pnwg.tools.diff.context;

import java.lang.reflect.Field;
import java.util.List;

import org.pnwg.tools.diff.handler.ITypeHandler;
import org.pnwg.tools.diff.listener.IDifferenceListner;
import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.helpers.IFieldProcessor;
import org.pnwg.tools.helpers.IFieldVisitor;
import org.pnwg.tools.helpers.Pair;

/**
 * 
 * @author Pawan
 *
 */
public interface IContext {

	/**
	 * Registers a Type handler for comparison
	 * 
	 * @param handler
	 * @return
	 */
	<T> IContext register(Class<T> clazz, ITypeHandler<T> handler);

	/**
	 * Register a custom DifferenceListner Calling this method multiple times
	 * registers multiple listeners.
	 * 
	 * @param listener
	 * @return
	 */
	IContext register(IDifferenceListner listener);

	/**
	 * Register a fully qualified field name for a feature.
	 * 
	 * @param feature
	 * @param fullyQualifiedField
	 * @return
	 */
	IContext register(FieldFeature feature, String... fullyQualifiedField);

	/**
	 * Register List of fully qualified field names for a feature.
	 * 
	 * @param feature
	 * @param fullyQualifiedField
	 * @return
	 */
	IContext register(FieldFeature feature, List<String> fullyQualifiedField);

	/**
	 * Register field visitor
	 * 
	 * @param fieldVisitor
	 * @return
	 */
	IContext register(IFieldVisitor fieldVisitor);

	/**
	 * Register field processor
	 * 
	 * @param fieldprocessor
	 * @return
	 */
	IContext register(IFieldProcessor fieldprocessor);

	/**
	 * Register base class after which comparison should stop and fields from
	 * super type should not be compared. Calling this method multiple times
	 * registers multiple classes.
	 * 
	 * @param clazz
	 * @return
	 */
	IContext registerBaseClass(Class<?> clazz);

	/**
	 * Get differences after the comparison has run.
	 * 
	 * @return
	 */
	List<Diff> getDifferences();

	/**
	 * 
	 * @param field
	 * @return
	 */
	boolean isIgnoreField(Field field);

	/**
	 * 
	 * @param field
	 * @return
	 */
	boolean isKeyField(Field field);

	/**
	 * Get registered comparison type handler for given class
	 * 
	 * @param clazz
	 * @return
	 */
	<T> ITypeHandler<T> getTypeHandler(Class<T> clazz);

	/**
	 * Get registered difference listeners
	 * 
	 * @return
	 */
	List<IDifferenceListner> getDifferenceListners();

	/**
	 * Add the difference
	 * 
	 * @param diff
	 */
	void addDiff(Diff diff);

	/**
	 * If the current run had any diffs
	 * 
	 * @return
	 */
	boolean hasDifferences();

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
	 * Add the pair to registry
	 * 
	 * @param pair
	 */
	void addPairToRegistry(Pair pair);

	/**
	 * Check if the {@link Pair} of expected and actual object has already been
	 * compared.
	 * 
	 * @param pair
	 * @return
	 */
	boolean checkPairRegistery(Pair pair);

	/**
	 * Reset the state of context.
	 */
	void reset();
}
