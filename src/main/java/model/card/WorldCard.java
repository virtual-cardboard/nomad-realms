package model.card;

import static java.lang.Math.max;
import static model.card.CardSerializationFormats.WORLD_CARD;

import java.util.List;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import derealizer.format.Derealizable;
import model.GameObject;
import model.id.WorldCardId;
import model.state.GameState;

public class WorldCard extends GameObject implements Derealizable {

	private GameCard card;
	private long collectionID;

	private int costModifier = 0;

	public WorldCard() {
	}

	public WorldCard(GameCard card, long collectionID) {
		this.card = card;
		this.collectionID = collectionID;
	}

	public WorldCard(long id, GameCard card, long collectionID) {
		super(id);
		this.card = card;
		this.collectionID = collectionID;
	}

	public WorldCard(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public void addTo(GameState state) {
		state.cards().put(id, this);
	}

	@Override
	public void removeFrom(GameState state) {
		state.cards().remove(id);
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
	public CardSerializationFormats formatEnum() {
		return WORLD_CARD;
	}

	@Override
	public void read(SerializationReader reader) {
		super.read(reader);
		this.card = GameCard.values()[reader.readInt()];
		this.collectionID = reader.readLong();
		if (reader.readBoolean()) {
			this.costModifier = reader.readInt();
		}
	}

	@Override
	public void write(SerializationWriter writer) {
		super.write(writer);
		writer.consume(card.ordinal());
		writer.consume(collectionID);
		if (costModifier == 0) {
			writer.consume(false);
		} else {
			writer.consume(true);
			writer.consume(costModifier);
		}
	}

}
