package org.simplug.framework.sample.alpha;

import org.simplug.framework.model.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReactOnGamma extends Plugin {

	private static final Logger LOG = LoggerFactory
			.getLogger(ReactOnGamma.class);

	@Override
	public void start() {
		LOG.info("Start up react_on_gamma in alpha-plugin.");
	}
}
