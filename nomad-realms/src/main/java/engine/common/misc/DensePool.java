package engine.common.misc;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * A generic, high-performance dense pool for managing object lifecycles in a contiguous array.
 * <p>
 * `DensePool` uses a dense contiguous array representation and O(1) swap-and-pop removal logic
 * when elements expire or are removed. This provides O(1) additions and O(activeCount) iteration passes
 * without frame-by-frame memory allocations or GC overhead.
 *
 * @param <T> the type of element managed by the pool
 */
public class DensePool<T> {

	private Object[] elements;
	private long[] creationTimes;
	private int size;

	public DensePool(int capacity) {
		this.elements = new Object[capacity];
		this.creationTimes = new long[capacity];
		this.size = 0;
	}

	/**
	 * Adds an item to the pool with the given creation timestamp.
	 *
	 * @param item         the item to add
	 * @param creationTime the creation time of the item
	 * @return true if added successfully, false if the pool is full or item is null
	 */
	public boolean add(T item, long creationTime) {
		if (item == null || size >= elements.length) {
			return false;
		}
		elements[size] = item;
		creationTimes[size] = creationTime;
		size++;
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
	 * by swapping it with the last active element in the array.
	 * If the filter returns true, the action consumer is called on the item.
	 *
	 * @param shouldKeep predicate returning true to keep and process the item, or false to expire/remove it
	 * @param action     consumer called for each active item that passes the predicate
	 */
	public void process(BiPredicate<T, Long> shouldKeep, Consumer<T> action) {
		int i = 0;
		while (i < size) {
			@SuppressWarnings("unchecked")
			T item = (T) elements[i];
			long creationTime = creationTimes[i];

			if (item == null || !shouldKeep.test(item, creationTime)) {
				size--;
				elements[i] = elements[size];
				creationTimes[i] = creationTimes[size];
				elements[size] = null;
				creationTimes[size] = 0;
				continue;
			}

			if (action != null) {
				action.accept(item);
			}
			i++;
		}
	}

	/**
	 * Returns the element at the given index in the dense array.
	 * Note: indices are valid from 0 to size() - 1.
	 */
	@SuppressWarnings("unchecked")
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
		}
		return (T) elements[index];
	}

	/**
	 * Returns the creation timestamp for the element at the given index.
	 */
	public long getCreationTime(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("Index " + index + " out of bounds for size " + size);
		}
		return creationTimes[index];
	}

	/**
	 * Returns the number of currently active items in the pool.
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns the maximum capacity of the pool.
	 */
	public int capacity() {
		return elements.length;
	}

	/**
	 * Clears all items from the pool.
	 */
	public void clear() {
		for (int i = 0; i < size; i++) {
			elements[i] = null;
			creationTimes[i] = 0;
		}
		size = 0;
	}

}
