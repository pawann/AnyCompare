package org.pnwg.tools.diff.context;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.pnwg.tools.diff.listener.IDifferenceListner;
import org.pnwg.tools.diff.model.Diff;
import org.pnwg.tools.diff.model.DiffType;
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

	private boolean listenersEnabled = true;

	private String currentLocation;

	private String currentKey;

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
	public void addDiff(List<Diff> diffs) {
		for (Diff d : diffs) {
			addDiffAndCallListeners(d);
		}
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

	@Override
	public IContext register(FieldFeature feature, String... fullyQualifiedField) {
		config().register(feature, fullyQualifiedField);
		return this;
	}

	@Override
	public IContext enableListeners(boolean value) {
		listenersEnabled = value;
		return this;
	}

	@Override
	public boolean isListenersEnabled() {
		return listenersEnabled;
	}

	@Override
	public String getCurrentLocation() {
		return this.currentLocation;
	}

	@Override
	public void setCurrentLocation(String location) {
		this.currentLocation = location;
	}

	@Override
	public void setCurrentKey(String key) {
		this.currentKey = key;

	}

	@Override
	public String getCurrentKey(String key) {
		return this.currentKey;
	}

	@Override
	public Diff onDifference(Object expectedObj, Object actualObj, Field field, DiffType type) {
		Diff diff = new Diff(expectedObj, actualObj, field, type);
		diff.setKey(currentKey);
		diff.setDiffLocation(currentLocation);

		addDiffAndCallListeners(diff);

		return diff;
	}

	private void addDiffAndCallListeners(Diff diff) {
		addDiff(diff);
		if (isListenersEnabled()) {
			for (IDifferenceListner listener : config().getDifferenceListners()) {
				listener.onDifference(diff);
			}
		}
	}

}
