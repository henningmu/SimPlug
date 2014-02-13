package org.simplug.framework.sample.alpha;

import org.simplug.framework.model.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReactOnAlpha extends Plugin {

	private static final Logger LOG = LoggerFactory
			.getLogger(ReactOnAlpha.class);

	@Override
	public void start() {
		LOG.info("Start up react_on_alpha in alpha-plugin.");
	}
}
