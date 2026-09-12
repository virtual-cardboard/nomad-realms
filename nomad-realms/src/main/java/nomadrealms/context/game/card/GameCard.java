package nomadrealms.context.game.card;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import engine.serialization.Derializable;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.card.target.TargetingInfo;

/**
 * Represents a card that can be played in the game. Each card has a title, description, expression, and
 * targeting info.
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
	public GameCard() {
	}

	public GameCard(String title, String artwork, String description, CardType type, int manaCost, int resolutionTime,
					CardExpression expression, TargetingInfo targetingInfo) {
		this.title = title;
		this.artwork = artwork;
		this.description = description;
		this.type = type;
		this.manaCost = manaCost;
		this.expression = expression;
		this.targetingInfo = targetingInfo;
		this.resolutionTime = resolutionTime;
	}

	public String title() {
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

	public TargetingInfo targetingInfo() {
		return targetingInfo;
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
				Objects.equals(title, gameCard.title) &&
				Objects.equals(artwork, gameCard.artwork) &&
				Objects.equals(description, gameCard.description) &&
				type == gameCard.type &&
				Objects.equals(keywords, gameCard.keywords);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, artwork, description, type, manaCost, resolutionTime, keywords);
	}

	@Override
	public String toString() {
		return "{" +
				"name='" + title +
				'}';
	}

}
