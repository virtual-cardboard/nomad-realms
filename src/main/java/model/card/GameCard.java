package model.card;

import static math.IDGenerator.genID;

import context.visuals.lwjgl.Texture;
import model.card.effect.CardEffect;

public class GameCard {

	private long id;
	private String name;
	private CardType type;
	private Texture texture;
	private CardRarity rarity;
	private CardEffect effect;
	private int resolutionTime;
	private String text;

	public GameCard(String name, CardType type, Texture texture, CardRarity rarity, CardEffect effect, int resolutionTime, String text) {
		this.id = genID();
		this.name = name;
		this.type = type;
		this.texture = texture;
		this.rarity = rarity;
		this.effect = effect;
		this.resolutionTime = resolutionTime;
		this.text = text;
	}

	public long id() {
		return id;
	}

	public String name() {
		return name;
	}

	public CardType type() {
		return type;
	}

	public Texture texture() {
		return texture;
	}

	public CardRarity rarity() {
		return rarity;
	}

	public CardEffect effect() {
		return effect;
	}

	public int resolutionTime() {
		return resolutionTime;
	}

	public String text() {
		return text;
	}

}
