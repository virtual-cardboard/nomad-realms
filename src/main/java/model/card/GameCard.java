package model.card;

import static model.card.CardRarity.ARCANE;
import static model.card.CardRarity.BASIC;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.expression.CardTargetType.CHARACTER;
import static model.card.expression.CardTargetType.TILE;

import model.card.condition.RangeCondition;
import model.card.expression.DealDamageExpression;
import model.card.expression.RegenesisExpression;
import model.card.expression.SelfDrawCardExpression;
import model.card.expression.TaskExpression;
import model.card.expression.TeleportExpression;
import model.task.MoveTask;

public enum GameCard {

	REGENESIS("Regenesis", 1, "When this card enters discard from anywhere, shuffle discard into deck.", ACTION, BASIC,
			new CardEffect(null, null, new RegenesisExpression())),
	ZAP("Zap", 0, "Deal 3 to target character within range 4.", CANTRIP, BASIC,
			new CardEffect(CHARACTER, new RangeCondition(4), new DealDamageExpression(3))),
	TELEPORT("Teleport", 0, "Teleport to target tile within radius 4.", CANTRIP, ARCANE,
			new CardEffect(TILE, null, new TeleportExpression())),
	MOVE("Test task", 0, "Move to target tile.", CardType.TASK, BASIC,
			new CardEffect(TILE, null, new TaskExpression(() -> new MoveTask()))),
	EXTRA_PREPARATION("Extra preparation", 1, "Draw 2.", ACTION, BASIC,
			new CardEffect(null, null, new SelfDrawCardExpression(2)));

	public final String name;
	public final int cost;
	public final String text;
	public final CardType type;
	public final CardRarity rarity;
	public final CardEffect effect;

	private GameCard(String name, int cost, String text, CardType type, CardRarity rarity, CardEffect effect) {
		this.name = name;
		this.cost = cost;
		this.text = text;
		this.type = type;
		this.rarity = rarity;
		this.effect = effect;
	}

}
