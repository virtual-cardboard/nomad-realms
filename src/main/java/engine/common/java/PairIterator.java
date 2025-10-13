package engine.common.java;

import java.util.Iterator;
import java.util.Map;

public class PairIterator<A, B> implements Iterator<Pair<A, B>> {

	private final Iterator<Map.Entry<A, B>> iterator;

	public PairIterator(Iterator<Map.Entry<A, B>> iterator) {
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public Pair<A, B> next() {
		Map.Entry<A, B> pair = iterator.next();
		return new Pair<>(pair.getKey(), pair.getValue());
	}

}
