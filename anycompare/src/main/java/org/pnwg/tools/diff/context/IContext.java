package org.pnwg.tools.diff.context;

import java.lang.reflect.Field;
import java.util.List;

import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.diff.model.DiffType;
import org.pnwg.tools.helpers.Pair;

/**
 * 
 * @author Pawan
 *
 */
public interface IContext {

	/**
	 * Get differences after the comparison has run.
	 * 
	 * @return
	 */
	List<Diff> getDifferences();

	/**
	 * Add the difference
	 * 
	 * @param diff
	 */
	void addDiff(Diff diff);

	/**
	 * Add list of difference
	 * 
	 * @param diff
	 */
	void addDiff(List<Diff> diffs);

	/**
	 * If the current run had any diffs
	 * 
	 * @return
	 */
	boolean hasDifferences();

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

	/**
	 * Get the current context config
	 */
	IConfig config();

	/**
	 * Turn on/off listers. If off, listeners would not be called on a
	 * difference.
	 * 
	 * @param value
	 * @return
	 */
	IContext enableListeners(boolean value);

	/**
	 * true if listeners are enabled.
	 * 
	 * @param value
	 * @return
	 */
	boolean isListenersEnabled();

	/**
	 * Register a fully qualified field name for a feature. fullyQualifiedField
	 * name is of the form "com.example.test.Person.age".
	 * 
	 * This is a convenience method that calls
	 * {@link IConfig#register(FieldFeature, String...)}
	 * 
	 * @param feature
	 * @param fullyQualifiedField
	 * @return
	 */
	IContext register(FieldFeature feature, String... fullyQualifiedField);

	/**
	 * get the current location of diff
	 * 
	 * @return
	 */
	String getCurrentLocation();

	/**
	 * Set the current location of diff
	 * 
	 * @param location
	 * @return
	 */
	void setCurrentLocation(String location);

	/**
	 * set the current key
	 * 
	 * @param key
	 */
	void setCurrentKey(String key);

	/**
	 * Get the current key
	 * 
	 * @param key
	 * @return
	 */
	String getCurrentKey(String key);

	/**
	 * Method that would be called when a difference is found.
	 * 
	 * @param expectedObj
	 * @param actualObj
	 * @param field
	 * @param type
	 * @return
	 */
	Diff onDifference(Object expectedObj, Object actualObj, Field field, DiffType type);
}
