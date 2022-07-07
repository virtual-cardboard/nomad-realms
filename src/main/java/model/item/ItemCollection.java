package model.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemCollection {

	private Map<Item, Integer> items;

	public ItemCollection(Map<Item, Integer> items) {
		this.items = items;
	}

	public ItemCollection() {
		this(new HashMap<>());
	}

	public ItemCollection(Item item, int amount) {
		items = new HashMap<>();
		items.put(item, amount);
	}

	public ItemCollection(ItemEntry... entries) {
		items = new HashMap<>();
		for (ItemEntry entry : entries) {
			items.put(entry.item, entry.amount);
		}
	}

	public int get(Item key) {
		Integer amount = items.get(key);
		amount = amount == null ? 0 : amount;
		return amount;
	}

	public Set<Item> keySet() {
		return items.keySet();
	}

	public boolean isSubcollectionOf(ItemCollection collection) {
		for (Item i : items.keySet()) {
			if (get(i) > collection.get(i)) {
				return false;
			}
		}
		return true;
	}

	public int put(Item key, int value) {
		if (value < 0) {
			throw new IllegalArgumentException("Cannot have a negative number of items: " + key + " " + value);
		}
		int prevValue = get(key);
		if (value == 0) {
			items.remove(key);
		} else {
			items.put(key, value);
		}
		return prevValue;
	}

	public int add(Item key, int value) {
		return put(key, get(key) + value);
	}

	public void add(ItemCollection collection) {
		for (Item i : collection.keySet()) {
			add(i, collection.get(i));
		}
	}

	public int sub(Item key, int value) {
		return put(key, get(key) - value);
	}

	public void sub(ItemCollection collection) {
		for (Item i : collection.keySet()) {
			sub(i, collection.get(i));
		}
	}

	public ItemCollection copy() {
		return new ItemCollection(new HashMap<>(items));
	}

}
