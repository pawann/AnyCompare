package org.pnwg.tools.diff.listener;

import org.pnwg.tools.diff.model.Diff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingDifferenceListener implements IDifferenceListner {

	public static Logger LOGGER = LoggerFactory.getLogger(LoggingDifferenceListener.class);

	@Override
	public void onDifference(Diff diff) {
		LOGGER.debug("Expected: {}, actual: {}, diffType:{} ", diff.getExpectedValue(), diff.getActualValue(),
				diff.getType());
	}

}
