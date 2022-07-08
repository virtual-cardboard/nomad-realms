package model.card;

import static model.GameObject.UNSET_ID;

public class CollectionCard {

	private GameCard type;
	private final long id;

	public CollectionCard(GameCard type) {
		this.type = type;
		id = UNSET_ID;
	}

	public GameCard card() {
		return type;
	}

	public String name() {
		return type.name;
	}

	public String text() {
		return type.text;
	}

	public int cost() {
		return type.cost;
	}

	public long collectionID() {
		return id;
	}

	public CollectionCard copy() {
		CollectionCard copy = new CollectionCard(type);
		return copy;
	}

}
