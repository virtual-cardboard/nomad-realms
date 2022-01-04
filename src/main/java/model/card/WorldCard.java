package model.card;

import java.util.List;
import java.util.Map;

import common.math.Vector2i;
import model.actor.Actor;
import model.actor.CardPlayer;
import model.actor.GameObject;

public class WorldCard extends GameObject {

	private String name;
	private CardType type;
	private CardRarity rarity;
	private CardEffect effect;
	private int cost;
	private String text;

	public WorldCard(String name, CardType type, CardRarity rarity, CardEffect effect, int resolutionTime, String text) {
		this.name = name;
		this.type = type;
		this.rarity = rarity;
		this.effect = effect;
		this.cost = resolutionTime;
		this.text = text;
	}

	public WorldCard(long id, String name, CardType type, CardRarity rarity, CardEffect effect, int resolutionTime, String text) {
		super(id);
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
	public void addTo(Map<Long, Actor> actors, Map<Long, CardPlayer> cardPlayers, Map<Long, WorldCard> cards, Map<Vector2i, List<Actor>> chunkToActors) {
		cards.put(id, this);
		super.addTo(actors, cardPlayers, cards, chunkToActors);
	}

	@Override
	public WorldCard copy() {
		WorldCard copy = new WorldCard(id, name, type, rarity, effect, cost, text);
		return copy;
	}

	public WorldCard copyDiffID() {
		return new WorldCard(name, type, rarity, effect, cost, text);
	}

	@Override
	public String description() {
		return toString();
	}

	@Override
	public String toString() {
		return "Card: " + name + " ID: " + id;
	}

}
