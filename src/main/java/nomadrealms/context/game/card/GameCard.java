package nomadrealms.context.game.card;

import static nomadrealms.context.game.card.target.TargetType.CARD_PLAYER;
import static nomadrealms.context.game.card.target.TargetType.HEXAGON;
import static nomadrealms.context.game.card.target.TargetType.NONE;

import nomadrealms.context.game.actor.structure.factory.StructureType;
import nomadrealms.context.game.card.condition.EmptyCondition;
import nomadrealms.context.game.card.expression.AndExpression;
import nomadrealms.context.game.card.expression.BuryAnySeedExpression;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.card.expression.CreateStructureExpression;
import nomadrealms.context.game.card.expression.DamageActorsExpression;
import nomadrealms.context.game.card.expression.DamageExpression;
import nomadrealms.context.game.card.expression.EditTileExpression;
import nomadrealms.context.game.card.expression.GatherExpression;
import nomadrealms.context.game.card.expression.MeleeDamageExpression;
import nomadrealms.context.game.card.expression.MoveExpression;
import nomadrealms.context.game.card.expression.SelfHealExpression;
import nomadrealms.context.game.card.expression.SurfaceCardExpression;
import nomadrealms.context.game.card.expression.TeleportExpression;
import nomadrealms.context.game.card.expression.TeleportNoTargetExpression;
import nomadrealms.context.game.card.query.actor.ActorsOnTilesQuery;
import nomadrealms.context.game.card.query.actor.SelfQuery;
import nomadrealms.context.game.card.query.actor.TargetQuery;
import nomadrealms.context.game.card.query.actor.TargetTypeCast;
import nomadrealms.context.game.card.query.card.LastResolvedCardQuery;
import nomadrealms.context.game.card.query.tile.PreviousTileQuery;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.render.particle.spawner.ParticleSpawner;

/**
 * An enum of all the cards that can be played in the game. Each card has a title, description, expression, and
 * targeting info.
 *
 * @author Lunkle
 */
public enum GameCard implements Card {

	MEANDER(
			"Meander",
			"move",
			"Move to target hexagon",
			new MoveExpression(10),
			new TargetingInfo(HEXAGON, 1,
					new EmptyCondition(new ActorsOnTilesQuery(new TargetTypeCast<Tile>(new TargetQuery()))))),
	ATTACK(
			"Attack",
			"big_punch",
			"Deal 2 to target character",
			new DamageExpression(2),
			new TargetingInfo(CARD_PLAYER, 1)),
	MOVE(
			"Move",
			"move",
			"Move to target hexagon.",
			new MoveExpression(10),
			new TargetingInfo(HEXAGON, 2,
					new EmptyCondition(new ActorsOnTilesQuery(new TargetTypeCast<Tile>(new TargetQuery()))))),
	UNSTABLE_TELEPORT(
			"Unstable Teleport",
			"teleport",
			"Teleport to target hexagon within range 3.",
			new TeleportExpression(10),
			new TargetingInfo(HEXAGON, 2)),
	REWIND(
			"Rewind",
			"teleport",
			"Teleport to the last hexagon you occupied. Surface the last card you played.",
			new AndExpression(
					new TeleportNoTargetExpression(new PreviousTileQuery(new SelfQuery()), 10),
					new SurfaceCardExpression(new LastResolvedCardQuery(new SelfQuery()), 10)),
			new TargetingInfo(NONE, 10)),
	HEAL(
			"Heal",
			"restore",
			"Restore 2 to self",
			new SelfHealExpression(2),
			new TargetingInfo(NONE, 10)),
	TILL_SOIL(
			"Till Soil",
			"regenesis",
			"Till the current tile",
			new EditTileExpression(TileType.SOIL),
			new TargetingInfo(HEXAGON, 10)),
	PLANT_SEED(
			"Plant seed",
			"regenesis",
			"Plant a seed on current tile",
			new BuryAnySeedExpression(),
			new TargetingInfo(NONE, 10)),
	GATHER(
			"Gather",
			"gather",
			"Gather items on current tile",
			new GatherExpression(1),
			new TargetingInfo(NONE, 1)),
	CREATE_ROCK(
			"Create Rock",
			"meteor",
			"Create a rock on target tile",
			new CreateStructureExpression(StructureType.ROCK),
			new TargetingInfo(HEXAGON, 1)),
	ELECTROSTATIC_ZAPPER(
			"Electrostatic Zapper",
			"overclocked_machinery",
			"Whenever a card is played within range 5, deal 2 to the source",
			new CreateStructureExpression(StructureType.ELECTROSTATIC_ZAPPER),
			new TargetingInfo(HEXAGON, 1)),
	MELEE_ATTACK(
			"Melee Attack",
			"bash",
			"Deal 2 melee damage to target character",
			new MeleeDamageExpression(2),
			new TargetingInfo(CARD_PLAYER, 1)),
	WOODEN_CHEST(
			"Wooden Chest",
			"overclocked_machinery",
			"Create a chest on target tile",
			new CreateStructureExpression(StructureType.CHEST),
			new TargetingInfo(HEXAGON, 1)),
	FLAME_CIRCLE(
			"Flame Circle",
			"meteor",
			"Deal 4 damage to all enemies within radius 3",
			new DamageActorsExpression(new ActorsOnTilesQuery(new TilesInRadiusQuery(3), true), 4),
			new TargetingInfo(NONE, 1)),
	ICE_CUBE(
			"Ice Cube",
			"ice_cube",
			"Does absolutely nothing.",
			new AndExpression(),
			new TargetingInfo(NONE, 0));

	private final String title;
	private final String artwork;
	private final String description;
	private final CardExpression expression;
	private final TargetingInfo targetingInfo;

	private GameCard(String name, String artwork, String description, CardExpression expression,
	                 TargetingInfo targetingInfo) {
		this.title = name;
		this.artwork = artwork;
		this.description = description;
		this.expression = expression;
		this.targetingInfo = targetingInfo;
	}

	private GameCard(String name, String artwork, String description, CardExpression expression, ParticleSpawner onResolve,
	                 TargetingInfo targetingInfo) {
		this.title = name;
		this.artwork = artwork;
		this.description = description;
		this.expression = expression;
		this.targetingInfo = targetingInfo;
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
