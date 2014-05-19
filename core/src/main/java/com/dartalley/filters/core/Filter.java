package com.dartalley.filters.core;

import java.util.function.Function;

public abstract class Filter<Input, Output, FilteredInput, FilteredOutput> {
	
	public Function<Input, Output> andThen(final Function<FilteredInput, FilteredOutput> function) {
		final Filter<Input, Output, FilteredInput, FilteredOutput> currentFilter = this;
		return new Function<Input, Output>() {
			public Output apply(Input in) {
				return currentFilter.apply(in, function);
			}
		};
	}
	
	public <A, B> Filter<Input, Output, A, B> andThen(final Filter<FilteredInput, FilteredOutput, A, B> filter) {
		final Filter<Input, Output, FilteredInput, FilteredOutput> currentFilter = this;
		return new Filter<Input, Output, A, B>() {
			@Override
			public Output apply(Input input, final Function<A, B> handler) {
				return currentFilter.apply(input, filter.andThen(handler));
			}
		};
	}
	
	public abstract Output apply(Input input, Function<FilteredInput, FilteredOutput> function);
}
