package nl.jessebrand.aoc;

import java.util.List;

public enum Operator {
	PLUS("+") {
		@Override
		public final long apply(final long a, final long b) {
			return a + b;
		}
	},
	MULTI("*") {
		@Override
		public final long apply(final long a, final long b) {
			return a * b;
		}
	};

	private final String symbol;

	private Operator(final String symbol) {
		this.symbol = symbol;
	}

	public abstract long apply(final long a, final long b);

	public final long apply(final List<Long> longs) {
		return longs.stream().reduce(this::apply).get();
	}

	public static final Operator from(final String symbol) {
		for (final Operator o : values()) {
			if (o.symbol.equals(symbol)) {
				return o;
			}
		}
		throw new IllegalArgumentException("Unknown symbol: " + symbol);
	}
}
