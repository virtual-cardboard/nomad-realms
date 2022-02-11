package model.item;

public class ItemEntry {

	public final Item item;
	public final int amount;

	public ItemEntry(Item item, int amount) {
		this.item = item;
		this.amount = amount;
	}

	public static ItemEntry entry(Item item, int amount) {
		return new ItemEntry(item, amount);
	}

}
