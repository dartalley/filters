package com.dartalley.filters.core;

import java.util.function.Function;


public class SleepFilter<Input, Output> extends SimpleFilter<Input, Output> {
	private final long millis;
	
	public SleepFilter(long millis) {
		this.millis = millis;
	}
	
	@Override
	public Output apply(Input input, Function<Input, Output> function) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return function.apply(input);
	}

}
