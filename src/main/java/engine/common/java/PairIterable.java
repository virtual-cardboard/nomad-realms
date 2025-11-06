package engine.common.java;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PairIterable<A, B> implements Iterable<Pair<A, B>> {

	private final Set<Map.Entry<A, B>> set;

	public PairIterable(Map<A, B> map) {
		this.set = map.entrySet();
	}

	@Override
	public Iterator<Pair<A, B>> iterator() {
		return new PairIterator<>(set.iterator());
	}

}
