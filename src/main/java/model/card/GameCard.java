package model.card;

import static model.card.CardRarity.ARCANE;
import static model.card.CardRarity.BASIC;
import static model.card.CardRarity.MUNDANE;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.CardType.STRUCTURE;
import static model.card.CardType.TASK;
import static model.card.expression.CardTargetType.CHARACTER;
import static model.card.expression.CardTargetType.TILE;

import model.actor.resource.TreeActor;
import model.card.condition.RangeCondition;
import model.card.expression.*;
import model.structure.StructureType;
import model.task.MoveTask;

public enum GameCard {

	GATHER("Gather", 0, "Gather all items within radius 5. Draw a card.", CardType.CANTRIP, BASIC,
			new CardEffect(null, null, new AndExpression(new GatherItemsExpression(5), new SelfDrawCardExpression(1)))),
	REGENESIS("Regenesis", 10, "When this card enters discard from anywhere, shuffle discard into deck.", ACTION, BASIC,
			new CardEffect(null, null, new RegenesisExpression())),
	ZAP("Zap", 0, "Deal 3 to target character within range 4.", CANTRIP, BASIC,
			new CardEffect(CHARACTER, new RangeCondition(4), new RangedDamageExpression(3))),
	TELEPORT("Teleport", 0, "Teleport to target tile within radius 4.", CANTRIP, ARCANE,
			new CardEffect(TILE, null, new TeleportExpression())),
	MOVE("Test Task", 0, "Move to target tile.", TASK, BASIC,
			new CardEffect(TILE, null, new TaskExpression(() -> new MoveTask()))),
	EXTRA_PREPARATION("Extra Preparation", 4, "Draw 3.", ACTION, BASIC,
			new CardEffect(null, null, new SelfDrawCardExpression(3))),
	CUT_TREE("Cut Tree", 5, "Destroy target tree within radius 5.", ACTION, BASIC,
			new CardEffect(CHARACTER, new RangeCondition(5).and((a, b) -> b instanceof TreeActor), new DestroyExpression())),
	OVERCLOCKED_MACHINERY("Overclocked Machinery", 2, "Whenever an action card is cast within radius 4, give it cost reduce 1.", STRUCTURE,
			MUNDANE, new CardEffect(TILE, null, new StructureExpression(StructureType.OVERCLOCKED_MACHINERY)));

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
