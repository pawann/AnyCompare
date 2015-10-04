package org.pnwg.tools.diff.context;

import java.util.List;

import org.pnwg.tools.diff.model.Diff;
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

}
