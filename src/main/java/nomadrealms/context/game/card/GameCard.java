package nomadrealms.context.game.card;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static nomadrealms.context.game.actor.status.StatusEffect.INVINCIBLE;
import static nomadrealms.context.game.actor.status.StatusEffect.POISON;
import static nomadrealms.context.game.card.target.TargetType.CARD_PLAYER;
import static nomadrealms.context.game.card.target.TargetType.HEXAGON;
import static nomadrealms.context.game.card.target.TargetType.NONE;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.context.game.world.map.tile.factory.TileType.SOIL;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.context.game.card.condition.EmptyCondition;
import nomadrealms.context.game.card.condition.RangeCondition;
import nomadrealms.context.game.card.expression.AddCardToStackExpression;
import nomadrealms.context.game.card.expression.AndExpression;
import nomadrealms.context.game.card.expression.ApplyStatusExpression;
import nomadrealms.context.game.card.expression.BuryAnySeedExpression;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.card.expression.CreateStructureExpression;
import nomadrealms.context.game.card.expression.DamageActorsExpression;
import nomadrealms.context.game.card.expression.DamageExpression;
import nomadrealms.context.game.card.expression.DashExpression;
import nomadrealms.context.game.card.expression.DelayedExpression;
import nomadrealms.context.game.card.expression.EditTileExpression;
import nomadrealms.context.game.card.expression.GatherExpression;
import nomadrealms.context.game.card.expression.MeleeDamageExpression;
import nomadrealms.context.game.card.expression.MoveExpression;
import nomadrealms.context.game.card.expression.RemoveStatusExpression;
import nomadrealms.context.game.card.expression.SelfHealExpression;
import nomadrealms.context.game.card.expression.SpawnParticlesExpression;
import nomadrealms.context.game.card.expression.SurfaceCardExpression;
import nomadrealms.context.game.card.expression.TeleportExpression;
import nomadrealms.context.game.card.expression.TeleportNoTargetExpression;
import nomadrealms.context.game.card.query.actor.ActorsOnTilesQuery;
import nomadrealms.context.game.card.query.actor.SelfQuery;
import nomadrealms.context.game.card.query.actor.StatusCountQuery;
import nomadrealms.context.game.card.query.actor.TargetQuery;
import nomadrealms.context.game.card.query.card.LastResolvedCardQuery;
import nomadrealms.context.game.card.query.math.LiteralQuery;
import nomadrealms.context.game.card.query.math.MinQuery;
import nomadrealms.context.game.card.query.math.RandomIntQuery;
import nomadrealms.context.game.card.query.tile.PreviousTileQuery;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.render.particle.spawner.BasicParticleSpawner;

/**
 * An enum of all the cards that can be played in the game. Each card has a title, description, expression, and
 * targeting info.
 *
 * @author Lunkle
 */
public enum GameCard implements Card {

	DASH(
			"Dash",
			"move",
			"Dash to target hexagon.",
			20,
			new DashExpression(5),
			new TargetingInfo(HEXAGON,
					new RangeCondition(1),
					new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
	MEANDER(
			"Meander",
			"move",
			"Move to target hexagon",
			20,
			new MoveExpression(10),
			new TargetingInfo(HEXAGON, new RangeCondition(1), new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
	ATTACK(
			"Attack",
			"big_punch",
			"Deal 2 to target character",
			20,
			new DamageExpression(2),
			new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
	MOVE(
			"Move",
			"move",
			"Move to target hexagon.",
			20,
			new MoveExpression(10),
			new TargetingInfo(HEXAGON,
					new RangeCondition(2),
					new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
	UNSTABLE_TELEPORT(
			"Unstable Teleport",
			"teleport",
			"Teleport to target hexagon within range 3.",
			20,
			new TeleportExpression(10),
			new TargetingInfo(HEXAGON, new RangeCondition(3),
					new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
	REWIND(
			"Rewind",
			"teleport",
			"Teleport to the last hexagon you occupied. Surface the last card you played.",
			20,
			new AndExpression(
					new TeleportNoTargetExpression(new PreviousTileQuery(new SelfQuery<>()), 10),
					new SurfaceCardExpression(new LastResolvedCardQuery(new SelfQuery<>()), 10)),
			new TargetingInfo(NONE)),
	HEAL(
			"Heal",
			"restore",
			"Restore 2 to self",
			10,
			new SelfHealExpression(2),
			new TargetingInfo(NONE)),
	TILL_SOIL(
			"Till Soil",
			"regenesis",
			"Till the current tile",
			20,
			new EditTileExpression(SOIL),
			new TargetingInfo(HEXAGON, new RangeCondition(10))),
	PLANT_SEED(
			"Plant seed",
			"regenesis",
			"Plant a seed on current tile",
			20,
			new BuryAnySeedExpression(),
			new TargetingInfo(NONE)),
	GATHER(
			"Gather",
			"gather",
			"Gather items on current tile",
			20,
			new GatherExpression(1),
			new TargetingInfo(NONE)),
	CREATE_ROCK(
			"Create Rock",
			"meteor",
			"Create a rock on target tile",
			20,
			new CreateStructureExpression(StructureType.ROCK),
			new TargetingInfo(HEXAGON, new RangeCondition(1), new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
	ELECTROSTATIC_ZAPPER(
			"Electrostatic Zapper",
			"overclocked_machinery",
			"Whenever a card is played within range 5, deal 2 to the source",
			20,
			new CreateStructureExpression(StructureType.ELECTROSTATIC_ZAPPER),
			new TargetingInfo(HEXAGON, new RangeCondition(1), new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
	MELEE_ATTACK(
			"Melee Attack",
			"bash",
			"Deal 2 melee damage to target character",
			20,
			new MeleeDamageExpression(2),
			new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
	WOODEN_CHEST(
			"Wooden Chest",
			"overclocked_machinery",
			"Create a chest on target tile",
			20,
			new CreateStructureExpression(StructureType.CHEST),
			new TargetingInfo(HEXAGON, new RangeCondition(1), new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
	FLAME_CIRCLE(
			"Flame Circle",
			"flame_circle",
			"Deal 4 damage to all enemies within radius 3",
			50,
			new DelayedExpression(
					new AndExpression(
							new SpawnParticlesExpression(
									new BasicParticleSpawner(new SelfQuery<>(), "fire_directional")
											.particleCount(20)
											.positionOffset(i -> new ConstraintPair(
													time().multiply(0.5f).multiply(sin(i * PI / 10)),
													time().multiply(0.5f).multiply(-cos(i * PI / 10))))
											.sizeOffset(i -> new ConstraintPair(
													absolute(12), absolute(18)))
											.rotation(i -> absolute(i * PI / 10))
											.lifetime(i -> (long) (3.5 * TILE_RADIUS / 0.5))
							),
							new DamageActorsExpression(new ActorsOnTilesQuery(new TilesInRadiusQuery(3), true), 4)),
					2, 5),
			new TargetingInfo(NONE)),
	ICE_CUBE(
			"Ice Cube",
			"ice_cube",
			"Does absolutely nothing.",
			20,
			new AndExpression(),
			new TargetingInfo(NONE)),
	FREEZE(
			"Freeze",
			"ice_cube",
			"Add an Ice Cube to the target's stack.",
			20,
			new AddCardToStackExpression(ICE_CUBE, new TargetQuery<>()),
			new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
	VENOMOUS_STRIKE(
			"Venomous Strike",
			"venomous_strike",
			"Deal 3 damage and apply 3 poison to target character",
			20,
			new AndExpression(
					new DamageExpression(3),
					new ApplyStatusExpression(new TargetQuery<>(), POISON, new LiteralQuery(3))
			),
			new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
	PURGE_POISON(
			"Purge Poison",
			"purge_poison",
			"Remove up to 10 poison from target character and deal that much damage to it",
			25,
			new AndExpression(
					new RemoveStatusExpression(POISON,
							new MinQuery(
									new StatusCountQuery(POISON, new TargetQuery<>()),
									new LiteralQuery(10)
							)
					),
					new DamageExpression(
							new MinQuery(
									new StatusCountQuery(POISON, new TargetQuery<>()),
									new LiteralQuery(10)
							)
					)
			),
			new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
	LIGHTNING_ZAP(
			"Lightning Zap",
			"zap",
			"Deal 2-4 damage to target character",
			20,
			new DamageExpression(new RandomIntQuery(2, 4)),
			new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
	INVINCIBILITY(
			"Invincibility",
			"restore",
			"Gain 1 invincible",
			10,
			new ApplyStatusExpression(new SelfQuery<>(), INVINCIBLE, new LiteralQuery(1)),
			new TargetingInfo(NONE)),
	TWIN_SCRATCHES(
			"Twin Scratches",
			"big_punch",
			"Scratch the target twice for 2 damage each.",
			20,
			new AndExpression(
					new SpawnParticlesExpression(
							new BasicParticleSpawner(new TargetQuery<>(), "scratch")
									.particleCount(2)
									.positionOffset(i -> new ConstraintPair(
											absolute((i == 0) ? -50 : 50).add(time().multiply((i == 0) ? 0.2f : -0.2f)),
											absolute(-50).add(time().multiply(0.2f))))
									.rotation(i -> absolute((i == 0) ? PI / 4 : -PI / 4))
									.lifetime(i -> 500L)
									.sizeOffset(i -> new ConstraintPair(absolute(100), absolute(100)))
					),
					new DelayedExpression(
							new AndExpression(
									new DamageExpression(2),
									new DelayedExpression(
											new DamageExpression(2),
											5, 0
									)
							),
							2, 0)),
			new TargetingInfo(CARD_PLAYER, new RangeCondition(1)));

	private final String title;
	private final String artwork;
	private final String description;
	private final CardExpression expression;
	private final TargetingInfo targetingInfo;
	private final int resolutionTime;

	private GameCard(String name, String artwork, String description, int resolutionTime, CardExpression expression,
	                 TargetingInfo targetingInfo) {
		this.title = name;
		this.artwork = artwork;
		this.description = description;
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

	public CardExpression expression() {
		return expression;
	}

	public TargetingInfo targetingInfo() {
		return targetingInfo;
	}

	public int resolutionTime() {
		return resolutionTime;
	}

	@Override
	public String toString() {
		return "{" +
				"name='" + title +
				'}';
	}

}
