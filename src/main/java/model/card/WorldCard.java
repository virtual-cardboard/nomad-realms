package model.card;

import static java.lang.Math.max;

import model.actor.GameObject;
import model.state.GameState;

public class WorldCard extends GameObject {

	private int costModifier = 0;
	private GameCard card;

	public WorldCard(GameCard card) {
		this.card = card;
	}

	public WorldCard(long id, GameCard card) {
		super(id);
		this.card = card;
	}

	@Override
	public void addTo(GameState state) {
		state.cards().put(id, this);
	}

	public String name() {
		return card.name;
	}

	public int cost() {
		return max(0, card.cost + costModifier);
	}

	public String text() {
		return card.text;
	}

	public CardType type() {
		return card.type;
	}

	public CardRarity rarity() {
		return card.rarity;
	}

	public CardEffect effect() {
		return card.effect;
	}

	public int costModifier() {
		return costModifier;
	}

	public void setCostModifier(int costModifier) {
		this.costModifier = costModifier;
	}

	@Override
	public WorldCard copy() {
		WorldCard copy = new WorldCard(id, card);
		copy.costModifier = costModifier;
		return copy;
	}

	public WorldCard copyDiffID() {
		WorldCard copy = new WorldCard(card);
		copy.costModifier = costModifier;
		return copy;
	}

	@Override
	public String description() {
		return toString();
	}

	@Override
	public String toString() {
		return "Card: " + name() + " ID: " + id;
	}

}
