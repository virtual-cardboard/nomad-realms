package nomadrealms.context.game.card;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import engine.serialization.Derializable;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.card.target.TargetingInfo;

/**
 * A class representing a card definition in the game. Each card has a title, artwork, description, type,
 * mana cost, resolution time, expression, and targeting info.
 *
 * @author Lunkle
 */
@Derializable
public class GameCard implements Card {

	private String title;
	private String artwork;
	private String description;
	private CardType type;
	private int manaCost;
	private CardExpression expression;
	private TargetingInfo targetingInfo;
	private int resolutionTime;
	private List<CardKeyword> keywords = new ArrayList<>();

	/**
	 * No-arg constructor for serialization.
	 */
	protected GameCard() {
	}

	public GameCard(String title, String artwork, String description, CardType type, int manaCost, int resolutionTime,
					CardExpression expression, TargetingInfo targetingInfo) {
		this.title = title;
		this.artwork = artwork;
		this.description = description;
		this.type = type;
		this.manaCost = manaCost;
		this.resolutionTime = resolutionTime;
		this.expression = expression;
		this.targetingInfo = targetingInfo;
	}

	public String title() {
		return title;
	}

	public String name() {
		return title;
	}

	public String artwork() {
		return artwork;
	}

	public String description() {
		return description;
	}

	public int manaCost() {
		return manaCost;
	}

	public CardExpression expression() {
		return expression;
	}

	public GameCard expression(CardExpression expression) {
		this.expression = expression;
		return this;
	}

	public TargetingInfo targetingInfo() {
		return targetingInfo;
	}

	public GameCard targetingInfo(TargetingInfo targetingInfo) {
		this.targetingInfo = targetingInfo;
		return this;
	}

	public int resolutionTime() {
		return resolutionTime;
	}

	public GameCard keywords(CardKeyword... keywords) {
		this.keywords.addAll(asList(keywords));
		return this;
	}

	@Override
	public List<CardKeyword> keywords() {
		return keywords;
	}

	@Override
	public CardType type() {
		return type;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GameCard gameCard = (GameCard) o;
		return manaCost == gameCard.manaCost &&
				resolutionTime == gameCard.resolutionTime &&
				java.util.Objects.equals(title, gameCard.title) &&
				java.util.Objects.equals(artwork, gameCard.artwork) &&
				java.util.Objects.equals(description, gameCard.description) &&
				type == gameCard.type;
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(title, artwork, description, type, manaCost, resolutionTime);
	}

	@Override
	public String toString() {
		return "{" +
				"name='" + title + '\'' +
				'}';
	}

}
