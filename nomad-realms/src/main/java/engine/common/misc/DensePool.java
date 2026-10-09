package engine.common.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * A generic, high-performance dense pool for managing object lifecycles backed by an ArrayList.
 * <p>
 * `DensePool` uses a dense `ArrayList` representation and O(1) swap-and-pop removal logic
 * when elements expire or are removed. This provides O(1) additions and O(activeCount) iteration passes
 * without frame-by-frame memory allocations or GC overhead.
 *
 * @param <T> the type of element managed by the pool
 */
public class DensePool<T> {

	private final int capacity;
	private final List<T> elements;
	private final List<Long> creationTimes;

	public DensePool(int capacity) {
		this.capacity = capacity;
		this.elements = new ArrayList<>(capacity);
		this.creationTimes = new ArrayList<>(capacity);
	}

	/**
	 * Adds an item to the pool with the given creation timestamp.
	 *
	 * @param item         the item to add
	 * @param creationTime the creation time of the item
	 * @return true if added successfully, false if the pool is full or item is null
	 */
	public boolean add(T item, long creationTime) {
		if (item == null || elements.size() >= capacity) {
			return false;
		}
		elements.add(item);
		creationTimes.add(creationTime);
		return true;
	}

	/**
	 * Adds an item to the pool using the current system time in milliseconds.
	 *
	 * @param item the item to add
	 * @return true if added successfully, false if full or item is null
	 */
	public boolean add(T item) {
		return add(item, System.currentTimeMillis());
	}

	/**
	 * Processes all active items in the pool.
	 * For each item, the filter predicate is tested with the item and its creation timestamp.
	 * If the filter returns false (or if the item should be removed), the item is removed in O(1) time
	 * by swapping it with the last element in the list and removing the last element.
	 * If the filter returns true, the action consumer is called on the item.
	 *
	 * @param shouldKeep predicate returning true to keep and process the item, or false to expire/remove it
	 * @param action     consumer called for each active item that passes the predicate
	 */
	public void process(BiPredicate<T, Long> shouldKeep, Consumer<T> action) {
		int i = 0;
		while (i < elements.size()) {
			T item = elements.get(i);
			long creationTime = creationTimes.get(i);

			if (item == null || !shouldKeep.test(item, creationTime)) {
				int lastIdx = elements.size() - 1;
				if (i < lastIdx) {
					elements.set(i, elements.get(lastIdx));
					creationTimes.set(i, creationTimes.get(lastIdx));
				}
				elements.remove(lastIdx);
				creationTimes.remove(lastIdx);
				continue;
			}

			if (action != null) {
				action.accept(item);
			}
			i++;
		}
	}

	/**
	 * Returns the element at the given index in the dense list.
	 * Note: indices are valid from 0 to size() - 1.
	 */
	public T get(int index) {
		return elements.get(index);
	}

	/**
	 * Returns the creation timestamp for the element at the given index.
	 */
	public long getCreationTime(int index) {
		return creationTimes.get(index);
	}

	/**
	 * Returns the number of currently active items in the pool.
	 */
	public int size() {
		return elements.size();
	}

	/**
	 * Returns the maximum capacity of the pool.
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Clears all items from the pool.
	 */
	public void clear() {
		elements.clear();
		creationTimes.clear();
	}

}
