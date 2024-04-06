package nomadrealms.game.card;

import nomadrealms.game.card.expression.CardExpression;
import nomadrealms.game.card.expression.DamageExpression;

public class GameCard implements Card {

	public static final GameCard MOVE = new GameCard("Move", "Move to target hexagon", null);
	public static final GameCard ATTACK = new GameCard("Attack", "Deal 2 to target character", new DamageExpression(2));
	public static final GameCard HEAL = new GameCard("Heal", "Restore 2 to target character", new DamageExpression(-2));

	private String name;
	private String description;
	private CardExpression expression;

	public GameCard(String name, String description, CardExpression expression) {
		this.name = name;
		this.description = description;
		this.expression = expression;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public CardExpression expression() {
		return expression;
	}

}
