package model.item;

import static java.lang.Integer.compare;

public class ItemCard implements Comparable<ItemCard> {

	private Item item;

	public ItemCard(Item item) {
		this.item = item;
	}

	public Item item() {
		return item;
	}

	@Override
	public int compareTo(ItemCard o) {
		return compare(item.ordinal(), o.item.ordinal());
	}

}
