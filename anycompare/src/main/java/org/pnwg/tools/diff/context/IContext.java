package org.pnwg.tools.diff.context;

import java.util.List;

import org.pnwg.tools.diff.handler.ITypeHandler;
import org.pnwg.tools.diff.listener.IDifferenceListner;
import org.pnwg.tools.diff.model.Diff;

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
	 * Register a custom DifferenceListner
	 * Calling this method multiple times registers multiple listeners.
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
	 * Register base class after which comparison should stop and fields from
	 * super type should not be compared. 
	 * Calling this method multiple times registers multiple classes.
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

}
