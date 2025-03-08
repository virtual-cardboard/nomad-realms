package nomadrealms.game.card;

import static nomadrealms.game.card.target.TargetType.CARD_PLAYER;
import static nomadrealms.game.card.target.TargetType.HEXAGON;
import static nomadrealms.game.card.target.TargetType.NONE;

import nomadrealms.game.actor.structure.factory.StructureType;
import nomadrealms.game.card.expression.BuryAnySeedExpression;
import nomadrealms.game.card.expression.CardExpression;
import nomadrealms.game.card.expression.CreateStructureExpression;
import nomadrealms.game.card.expression.DamageExpression;
import nomadrealms.game.card.expression.EditTileExpression;
import nomadrealms.game.card.expression.GatherExpression;
import nomadrealms.game.card.expression.MeleeDamageExpression;
import nomadrealms.game.card.expression.MoveExpression;
import nomadrealms.game.card.expression.SelfHealExpression;
import nomadrealms.game.card.expression.ReshuffleDecksExpression;
import nomadrealms.game.card.target.TargetingInfo;
import nomadrealms.game.world.map.tile.factory.TileType;

public enum GameCard implements Card {

	MEANDER(
			"Meander",
			"Move to target hexagon",
			new MoveExpression(10),
			new TargetingInfo(HEXAGON, 1)),
	ATTACK(
			"Attack",
			"Deal 2 to target character",
			new DamageExpression(2),
			new TargetingInfo(CARD_PLAYER, 1)),
	MOVE(
			"Move",
			"Move to target hexagon",
			new MoveExpression(10),
			new TargetingInfo(HEXAGON, 2)),
	HEAL(
			"Heal",
			"Restore 2 to self",
			new SelfHealExpression(2),
			new TargetingInfo(NONE, 10)),
	TILL_SOIL(
			"Till Soil",
			"Till the current tile",
			new EditTileExpression(TileType.SOIL),
			new TargetingInfo(HEXAGON, 10)),
	PLANT_SEED(
			"Plant seed",
			"Plant a seed on current tile",
			new BuryAnySeedExpression(),
			new TargetingInfo(NONE, 10)),
	GATHER(
			"Gather",
			"Gather items on current tile",
			new GatherExpression(1),
			new TargetingInfo(NONE, 1)),
	CREATE_ROCK(
			"Create Rock",
			"Create a rock on target tile",
			new CreateStructureExpression(StructureType.ROCK),
			new TargetingInfo(HEXAGON, 1)),
	ELECTROSTATIC_ZAPPER(
			"Electrostatic Zapper",
			"Whenever a card is played within range 5, deal 2 to the source",
			new CreateStructureExpression(StructureType.ELECTROSTATIC_ZAPPER),
			new TargetingInfo(HEXAGON, 1)),
	MELEE_ATTACK(
			"Melee Attack",
			"Deal 2 melee damage to target character",
			new MeleeDamageExpression(2),
			new TargetingInfo(CARD_PLAYER, 1)),
	RESHUFFLE_DECKS(
			"Reshuffle Decks",
			"Reshuffle all your decks",
			new ReshuffleDecksExpression(),
			new TargetingInfo(NONE, 1));

	private final String title;
	private final String description;
	private final CardExpression expression;
	private final TargetingInfo targetingInfo;

	private GameCard(String name, String description, CardExpression expression, TargetingInfo targetingInfo) {
		this.title = name;
		this.description = description;
		this.expression = expression;
		this.targetingInfo = targetingInfo;
	}

	public String title() {
		return title;
	}

	public String description() {
		return description;
	}

	public CardExpression expression() {
		return expression;
	}

	public TargetingInfo targetingInfo() {
		return targetingInfo;
	}

	@Override
	public String toString() {
		return "{" +
				"name='" + title +
				'}';
	}

}
