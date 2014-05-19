package com.dartalley.filters.metrics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer.Context;
import com.dartalley.filters.core.SimpleFilter;
import com.dartalley.filters.metrics.EndpointMetricSet;

public class MetricFilter<Input, Output> extends SimpleFilter<Input, Output> {
    // Cache allows us to ignore creating the same metric over and over.
    private static final Map<String, EndpointMetricSet> cache = new ConcurrentHashMap<String, EndpointMetricSet>();
	private final EndpointMetricSet metrics;
	
	public MetricFilter(MetricRegistry registry, String name) {
	    if (cache.containsKey(name)) {
	        this.metrics = cache.get(name);
	    } else {
	        this.metrics = new EndpointMetricSet(name);
	        registry.registerAll(metrics);
	        cache.put(name, this.metrics);
	    }
	}

	@Override
	public Output apply(Input input, Function<Input, Output> function) {
		Context timerContext = metrics.start();
		boolean exception = true;
		try {
			Output out = function.apply(input);
			exception = false;
			return out;
		} finally {
			if (exception) {
				metrics.exception(timerContext);
			} else {
				metrics.mark(timerContext);
			}
		}
	}
}
