package org.pnwg.tools.diff.context;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.helpers.Pair;

/**
 * Running state of comparison
 * 
 * @author Pawan
 *
 */
public class ContextImpl implements IContext {

	private IConfig _config = new ConfigImpl();

	private List<Diff> diffs = new LinkedList<>();

	private Set<Pair> pairRegistry = new HashSet<>();

	private boolean foundDiffs = false;

	@Override
	public List<Diff> getDifferences() {
		return diffs;
	}

	@Override
	public void addDiff(Diff diff) {
		foundDiffs = true;
		diffs.add(diff);
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

	@Override
	public IConfig config() {
		return _config;
	}

	protected void setConfig(IConfig cfg) {
		this._config = cfg;
	}
}
