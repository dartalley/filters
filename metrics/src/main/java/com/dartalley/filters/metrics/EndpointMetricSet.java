package com.dartalley.filters.metrics;

import java.util.HashMap;
import java.util.Map;

import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.Timer;

public class EndpointMetricSet implements MetricSet {
	private final Meter requestMeter;
	private final Meter exceptionMeter; 
	private final Timer requestTimer;
	private final String name;
	
	public EndpointMetricSet(String name) {
		this.requestMeter = new Meter();
		this.exceptionMeter = new Meter();
		this.requestTimer = new Timer();
		this.name = name;
	}
	
	public Timer.Context start() {
		return requestTimer.time();
	}
	
	public void mark(Timer.Context timerContext) {
		requestMeter.mark();
		timerContext.close();
	}
	
	public void exception(Timer.Context timerContext) {
		exceptionMeter.mark();
		mark(timerContext);
	}
	
	@Override
	public Map<String, Metric> getMetrics() {
		Map<String, Metric> metrics = new HashMap<String, Metric>();
		metrics.put("endpoint." + name + ".requests", requestMeter);
		metrics.put("endpoint." + name + ".exceptions", exceptionMeter);
		metrics.put("endpoint." + name + ".timer", requestTimer);
		return metrics;
	}
}
