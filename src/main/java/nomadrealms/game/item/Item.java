package nomadrealms.game.item;

import static java.util.Arrays.asList;
import static nomadrealms.game.item.ItemTag.ORGANIC;
import static nomadrealms.game.item.ItemTag.SEED;

import java.util.ArrayList;
import java.util.List;

public class Item {

	public static final Item WHEAT_SEED = new Item("Wheat Seed", "wheat_seed", "Grows into a bundle of wheat.", SEED, ORGANIC);
	public static final Item OAK_LOG = new Item("Oak Log", "oak_log", "A segment of the trunk of an oak tree.", ORGANIC);

	private final String name;
	private final String image;
	private final String description;
	private final List<ItemTag> tags;

	/**
	 * No-arg constructor for serialization.
	 */
	protected Item() {
		this(null, null, null);
	}

	public Item(String name, String image, String description, ItemTag... tags) {
		this.name = name;
		this.image = image;
		this.description = description;
		this.tags = new ArrayList<>(asList(tags));
	}

	public String name() {
		return name;
	}

	public String image() {
		return image;
	}

	public String description() {
		return description;
	}

	public List<ItemTag> tags() {
		return tags;
	}

	@Override
	public String toString() {
		return "{" +
				"name='" + name +
				'}';
	}

}