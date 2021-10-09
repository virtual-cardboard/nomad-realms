package model.card;

import static math.IDGenerator.genID;

import model.card.effect.CardEffect;

public class GameCard {

	private long id;
	private String name;
	private CardRarity rarity;
	private CardEffect effect;

	public GameCard(String name, CardEffect effect) {
		this.id = genID();
		this.name = name;
		this.effect = effect;
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

	public CardEffect effect() {
		return effect;
	}

}
