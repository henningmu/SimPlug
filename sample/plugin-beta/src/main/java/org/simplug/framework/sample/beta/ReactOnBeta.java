package org.simplug.framework.sample.beta;

import org.simplug.framework.model.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReactOnBeta extends Plugin {

	private static final Logger LOG = LoggerFactory
			.getLogger(ReactOnBeta.class);

	@Override
	public void start() {
		LOG.info("Start up react_on_beta in beta-plugin.");
	}
}
