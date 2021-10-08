package model.card;

import static math.IDGenerator.genID;

public class GameCard {

	private long id;
	private String name;
	private CardRarity rarity;

	public GameCard(String name) {
		this.id = genID();
		this.name = name;
	}

	public long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public CardRarity rarity() {
		return rarity;
	}

}
