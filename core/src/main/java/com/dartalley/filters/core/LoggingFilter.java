package com.dartalley.filters.core;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingFilter<Input, Output> extends SimpleFilter<Input, Output> {
	private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	private String name;
	private TracingProvider traceProvider;

	public LoggingFilter(String name) {
		this(name, new ToStringTracingProvider());
	}

	public LoggingFilter(String name, TracingProvider traceProvider) {
		super();
		this.name = name;
		this.traceProvider = traceProvider;
	}

	@Override
	public Output apply(Input input, Function<Input, Output> function) {
		try {
			Output output = function.apply(input);
			if (logger.isTraceEnabled()) {
				logger.trace(getLoggingString(input, output));
			}
			return output;
		} catch (Exception ex) {
			if (logger.isTraceEnabled()) {
				logger.trace(getLoggingStringWithException(input, ex), ex);
			}
			logger.warn(name + " failed with a " + ex.getClass().getSimpleName() + " exception.", ex);
			throw ex;
		}
	}

	String getLoggingString(Input input, Output output) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n***** Begin: " + name + "*****\n");
		sb.append("Input: \n" + traceProvider.getTrace(input) + "\n");
		sb.append("Output: \n" + traceProvider.getTrace(output) + "\n");
		sb.append("***** End: " + name + "*****\n");
		return sb.toString();
	}

	String getLoggingStringWithException(Input input, Exception ex) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n***** Begin: " + name + "*****\n");
		sb.append("Input: \n" + traceProvider.getTrace(input) + "\n");
		sb.append("Exception: \n" + ex + "\n");
		sb.append("***** End: " + name + "*****\n");
		return sb.toString();
	}

	public static interface TracingProvider {
		String getTrace(Object obj);
	}

	private static class ToStringTracingProvider implements TracingProvider {
		@Override
		public String getTrace(Object obj) {
			if (null == obj) {
				return "null";
			}
			return obj.toString();
		}

	}
}
