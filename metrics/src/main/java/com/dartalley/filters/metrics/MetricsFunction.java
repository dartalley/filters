package com.dartalley.filters.metrics;

import java.util.function.Function;

import com.codahale.metrics.MetricRegistry;

public class MetricsFunction implements Function<Void, MetricRegistry> {
	private final MetricRegistry registry;
	
	public MetricsFunction(MetricRegistry registry) {
		super();
		this.registry = registry;
	}

	@Override
	public MetricRegistry apply(Void input) {
		return registry;
	}
}
