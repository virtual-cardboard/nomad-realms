package engine.common.misc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * A class providing runtime type information for a list of input parameters.
 * <br>
 * <br>
 * This is not a performant class, and should not be used in performance-critical code.
 */
public class DataList<T extends Data> implements Iterable<T> {

	protected final Map<String, Integer> parameterIndices;
	protected final List<T> parameters;

	public DataList(List<T> parameters) {
		this.parameters = parameters;
		parameterIndices = new HashMap<>(parameters.size());
		for (int i = 0, m = parameters.size(); i < m; i++) {
			Data parameter = parameters.get(i);
			parameterIndices.put(parameter.name(), i);
		}
	}

	/**
	 * Returns an iterator over the data elements in this list in proper sequence.
	 * <br>
	 * <br>
	 * Internally delegates to the underlying list.
	 */
	@Override
	public Iterator<T> iterator() {
		return parameters.iterator();
	}

	/**
	 * Performs the given action for each data element until all elements have been processed or the action throws an
	 * exception. Unless otherwise specified by the implementing class, actions are performed in the order of iteration
	 * (if an iteration order is specified). Exceptions thrown by the action are relayed to the caller.
	 * <br>
	 * <br>
	 * Internally delegates to the underlying list.
	 */
	@Override
	public void forEach(Consumer<? super T> action) {
		parameters.forEach(action);
	}

	/**
	 * Creates a {@link Spliterator} over the elements in this list.
	 * <br>
	 * <br>
	 * Internally delegates to the underlying list.
	 */
	@Override
	public Spliterator<T> spliterator() {
		return parameters.spliterator();
	}

	@Override
	public String toString() {
		return "DataList [parameters=" + parameters + "]";
	}

}
