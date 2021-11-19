package model.card;

import model.actor.Actor;
import model.card.effect.CardEffect;

public class GameCard extends Actor {

	private String name;
	private CardType type;
	private CardRarity rarity;
	private CardEffect effect;
	private int cost;
	private String text;

	public GameCard(String name, CardType type, CardRarity rarity, CardEffect effect, int resolutionTime, String text) {
		this.name = name;
		this.type = type;
		this.rarity = rarity;
		this.effect = effect;
		this.cost = resolutionTime;
		this.text = text;
	}

	public String name() {
		return name;
	}

	public CardType type() {
		return type;
	}

	public CardRarity rarity() {
		return rarity;
	}

	public CardEffect effect() {
		return effect;
	}

	public int cost() {
		return cost;
	}

	public int visualCost() {
		return 10 * cost;
	}

	public String text() {
		return text;
	}

	@Override
	public GameCard copy() {
		return new GameCard(name, type, rarity, effect, cost, text);
	}

}
