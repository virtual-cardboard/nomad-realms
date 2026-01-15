package nomadrealms.context.game.item;

import static java.util.Arrays.asList;
import static nomadrealms.context.game.item.ItemTag.ORGANIC;
import static nomadrealms.context.game.item.ItemTag.SEED;

import java.util.ArrayList;
import java.util.List;

public enum Item {

	WHEAT_SEED("Wheat Seed", "wheat_seed", "Grows into a bundle of wheat.", SEED, ORGANIC),
	OAK_LOG("Oak Log", "oak_log", "A segment of the trunk of an oak tree.", ORGANIC),
	;

	private final String title;
	private final String image;
	private final String description;
	private final List<ItemTag> tags;

	/**
	 * No-arg constructor for serialization.
	 */
	Item() {
		this(null, null, null);
	}

	Item(String title, String image, String description, ItemTag... tags) {
		this.title = title;
		this.image = image;
		this.description = description;
		this.tags = new ArrayList<>(asList(tags));
	}

	public String title() {
		return title;
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
				"name='" + title +
				'}';
	}

}