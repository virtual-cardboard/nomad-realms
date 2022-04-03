package model.card;

import static java.util.Collections.unmodifiableList;
import static model.actor.HealthActor.isHealthActor;
import static model.card.CardEffectBuilder.effectBuilder;
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
import model.card.expression.AndExpression;
import model.card.expression.DestroyExpression;
import model.card.expression.GatherItemsExpression;
import model.card.expression.InteractExpression;
import model.card.expression.MeleeDamageExpression;
import model.card.expression.RangedDamageExpression;
import model.card.expression.RegenesisExpression;
import model.card.expression.RestoreExpression;
import model.card.expression.DrawCardExpression;
import model.card.expression.StructureExpression;
import model.card.expression.TaskExpression;
import model.card.expression.TeleportExpression;
import model.item.ItemCollection;
import model.structure.StructureType;
import model.task.MoveTask;

public enum GameCard {

	GATHER("Gather", 0, "Gather all items within radius 5. Draw a card.", CANTRIP, BASIC,
			effectBuilder()
					.expression(new AndExpression(new GatherItemsExpression(5), new DrawCardExpression(1)))
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
					.expression(new DrawCardExpression(2))
					.build()),
	BASH("Bash", 2, "Deal 5 to target enemy within radius 2.", ACTION, BASIC,
			effectBuilder()
					.targetType(CHARACTER)
					.targetPredicate(new RangeCondition(2).and(isHealthActor()))
					.expression(new MeleeDamageExpression(5))
					.build()),
	REFRESHING_BREAK("Refreshing Break", 4, "Restore 2. Draw 1.", ACTION, BASIC,
			effectBuilder()
					.expression(new AndExpression(new RestoreExpression(2), new DrawCardExpression(1)))
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
					.build()),
	PLANNING_TABLE("Planning Table", 2, "Interact - Source edits their deck.", STRUCTURE, BASIC,
			effectBuilder()
					.targetType(TILE)
					.expression(new StructureExpression(StructureType.PLANNING_TABLE))
					.build()),
	INTERACT("Interact", 0, "Interact with target.", CANTRIP, BASIC,
			effectBuilder()
					.targetType(CHARACTER)
					.expression(new InteractExpression())
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

}
