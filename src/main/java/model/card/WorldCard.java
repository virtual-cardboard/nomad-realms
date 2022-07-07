package model.card;

import static java.lang.Math.max;

import java.util.List;

import derealizer.format.SerializationFormatEnum;
import model.GameObject;
import model.id.WorldCardId;
import model.state.GameState;

public class WorldCard extends GameObject {

	private GameCard card;
	private final long collectionID;

	private int costModifier = 0;

	public WorldCard(GameCard card, long collectionID) {
		this.card = card;
		this.collectionID = collectionID;
	}

	public WorldCard(long id, GameCard card, long collectionID) {
		super(id);
		this.card = card;
		this.collectionID = collectionID;
	}

	@Override
	public void addTo(GameState state) {
		state.cards().put(id, this);
	}

	@Override
	public WorldCardId id() {
		return new WorldCardId(id);
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

	public GameCard card() {
		return card;
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
		WorldCard copy = new WorldCard(id, card, collectionID);
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

	public List<CardTag> tags() {
		return card.tags;
	}

	@Override
	public SerializationFormatEnum formatEnum() {
		return null;
	}

}
