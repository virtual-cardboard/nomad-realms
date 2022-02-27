package model.card;

import static java.util.Collections.unmodifiableList;
import static model.actor.HealthActor.isHealthActor;
import static model.card.CardRarity.ARCANE;
import static model.card.CardRarity.BASIC;
import static model.card.CardRarity.MUNDANE;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.CardType.STRUCTURE;
import static model.card.CardType.TASK;
import static model.card.expression.CardTargetType.CHARACTER;
import static model.card.expression.CardTargetType.TILE;
import static model.item.Item.WOOD;

import java.util.List;

import model.actor.resource.TreeActor;
import model.card.condition.RangeCondition;
import model.card.expression.*;
import model.item.ItemCollection;
import model.structure.StructureType;
import model.task.MoveTask;

public enum GameCard {

	GATHER("Gather", 0, "Gather all items within radius 5. Draw a card.", CANTRIP, BASIC,
			effectBuilder()
					.expression(new AndExpression(new GatherItemsExpression(5), new SelfDrawCardExpression(1)))
					.build()),
	REGENESIS("Regenesis", 2, "When this card enters discard from anywhere, shuffle discard into deck.", ACTION, BASIC,
			effectBuilder()
					.expression(new RegenesisExpression())
					.build()),
	ZAP("Zap", 0, "Deal 3 to target character within range 4.", CANTRIP, BASIC,
			effectBuilder()
					.targetType(CHARACTER)
					.targetPredicate(new RangeCondition(4).and(isHealthActor()))
					.expression(new RangedDamageExpression(3))
					.build()),
	TELEPORT("Teleport", 0, "Teleport to target tile within radius 4.", CANTRIP, ARCANE,
			effectBuilder()
					.targetType(TILE)
					.expression(new TeleportExpression())
					.build()),
	MOVE("Move", 0, "Move to target tile.", TASK, BASIC,
			effectBuilder()
					.targetType(TILE)
					.expression(new TaskExpression(MoveTask::new))
					.build()),
	EXTRA_PREPARATION("Extra Preparation", 2, "Draw 2.", ACTION, BASIC,
			effectBuilder()
					.expression(new SelfDrawCardExpression(2))
					.build()),
	CUT_TREE("Cut Tree", 2, "Destroy target tree within radius 5.", ACTION, BASIC,
			effectBuilder()
					.targetType(CHARACTER)
					.targetPredicate(new RangeCondition(5).and((a, b) -> b instanceof TreeActor))
					.expression(new DestroyExpression())
					.build()),
	HOUSE("House", 1, "This is a house", STRUCTURE, BASIC,
			effectBuilder()
					.requiredItems(new ItemCollection(WOOD, 1))
					.targetType(TILE)
					.expression(new StructureExpression(StructureType.HOUSE))
					.build()),
	OVERCLOCKED_MACHINERY("Overclocked Machinery", 2, "Whenever an action card is cast within radius 4, give it cost reduce 1.", STRUCTURE, MUNDANE,
			effectBuilder()
					.targetType(TILE)
					.expression(new StructureExpression(StructureType.OVERCLOCKED_MACHINERY))
					.build());

	public final String name;
	public final int cost;
	public final String text;
	public final CardType type;
	public final CardRarity rarity;
	public final CardEffect effect;

	public final List<CardTag> tags;

	private GameCard(String name, int cost, String text, CardType type, CardRarity rarity, CardEffect effect) {
		this.name = name;
		this.cost = cost;
		this.text = text;
		this.type = type;
		this.rarity = rarity;
		this.effect = effect;
		this.tags = unmodifiableList(effect.getTags());
	}

	private static CardEffectBuilder effectBuilder() {
		return new CardEffectBuilder();
	}

}
