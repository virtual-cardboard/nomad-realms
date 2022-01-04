package model.card;

import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.GameObject;

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
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, WorldCard> cards, Map<Vector2i, List<Actor>> chunkToActors) {
		cards.put(id, this);
		super.addTo(actors, cardPlayers, cards, chunkToActors);
	}

	public String name() {
		return card.name;
	}

	public int cost() {
		return card.cost;
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
