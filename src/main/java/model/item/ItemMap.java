package model.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemMap {

	private Map<Item, Integer> items;

	public ItemMap(Map<Item, Integer> items) {
		this.items = items;
	}

	public ItemMap() {
		this(new HashMap<>());
	}

	public int get(Object key) {
		Integer amount = items.get(key);
		amount = amount == null ? 0 : amount;
		return amount;
	}

	public Set<Item> keySet() {
		return items.keySet();
	}

	public int put(Item key, int value) {
		int prevValue = get(key);
		if (value == 0) {
			items.remove(key);
		} else {
			items.put(key, value);
		}
		return prevValue;
	}

	public int size() {
		return items.size();
	}

}
