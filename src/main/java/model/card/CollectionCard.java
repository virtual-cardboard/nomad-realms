package model.card;

public class CollectionCard {

	private GameCard type;

	public CollectionCard(GameCard type) {
		this.type = type;
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

}
