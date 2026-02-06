package nomadrealms.context.game.card;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.context.game.card.condition.EmptyCondition;
import nomadrealms.context.game.card.condition.ExactRangeCondition;
import nomadrealms.context.game.card.condition.RangeCondition;
import nomadrealms.context.game.card.expression.CardExpression;
import nomadrealms.context.game.card.expression.DamageActorsExpression;
import nomadrealms.context.game.card.expression.SpawnParticlesExpression;
import nomadrealms.context.game.card.query.actor.ActorsOnTilesQuery;
import nomadrealms.context.game.card.query.actor.SelfQuery;
import nomadrealms.context.game.card.query.actor.StatusCountQuery;
import nomadrealms.context.game.card.query.actor.TargetQuery;
import nomadrealms.context.game.card.query.card.LastResolvedCardQuery;
import nomadrealms.context.game.card.query.math.LiteralQuery;
import nomadrealms.context.game.card.query.math.MinQuery;
import nomadrealms.context.game.card.query.math.RandomIntQuery;
import nomadrealms.context.game.card.query.tile.PreviousTileQuery;
import nomadrealms.context.game.card.query.tile.TileQuery;
import nomadrealms.context.game.card.query.tile.TilesInRadiusQuery;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.render.particle.spawner.BasicParticleSpawner;

import static engine.visuals.constraint.misc.TimedConstraint.time;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.lang.Math.*;
import static nomadrealms.context.game.actor.status.StatusEffect.INVINCIBLE;
import static nomadrealms.context.game.actor.status.StatusEffect.POISON;
import static nomadrealms.context.game.card.expression.AddCardToStackExpression.addCardToStack;
import static nomadrealms.context.game.card.expression.AndExpression.and;
import static nomadrealms.context.game.card.expression.ApplyStatusExpression.applyStatus;
import static nomadrealms.context.game.card.expression.BuryAnySeedExpression.buryAnySeed;
import static nomadrealms.context.game.card.expression.CreateStructureExpression.createStructure;
import static nomadrealms.context.game.card.expression.DamageExpression.damage;
import static nomadrealms.context.game.card.expression.DashExpression.dash;
import static nomadrealms.context.game.card.expression.DelayedExpression.delayed;
import static nomadrealms.context.game.card.expression.EditTileExpression.editTile;
import static nomadrealms.context.game.card.expression.GatherExpression.gather;
import static nomadrealms.context.game.card.expression.MeleeDamageExpression.meleeDamage;
import static nomadrealms.context.game.card.expression.MoveExpression.move;
import static nomadrealms.context.game.card.expression.RemoveStatusExpression.removeStatus;
import static nomadrealms.context.game.card.expression.SelfHealExpression.selfHeal;
import static nomadrealms.context.game.card.expression.SurfaceCardExpression.surfaceCard;
import static nomadrealms.context.game.card.expression.TeleportExpression.teleport;
import static nomadrealms.context.game.card.expression.TeleportNoTargetExpression.teleport;
import static nomadrealms.context.game.card.target.TargetType.*;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.context.game.world.map.tile.factory.TileType.SOIL;

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
            dash(5),
            new TargetingInfo(HEXAGON,
                    new RangeCondition(1),
                    new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
    MEANDER(
            "Meander",
            "move",
            "Move to target hexagon",
            20,
            move(10),
            new TargetingInfo(HEXAGON, new RangeCondition(1), new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
    ATTACK(
            "Attack",
            "big_punch",
            "Deal 2 to target character",
            20,
            damage(2),
            new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
    MOVE(
            "Move",
            "move",
            "Move to target hexagon.",
            20,
            move(10),
            new TargetingInfo(HEXAGON,
                    new RangeCondition(2),
                    new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
    UNSTABLE_TELEPORT(
            "Unstable Teleport",
            "teleport",
            "Teleport to target hexagon within range 3.",
            20,
            teleport(10),
            new TargetingInfo(HEXAGON, new RangeCondition(3),
                    new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
    REWIND(
            "Rewind",
            "teleport",
            "Teleport to the last hexagon you occupied. Surface the last card you played.",
            20,
            and(
                    teleport(new PreviousTileQuery(new SelfQuery<>()), 10),
                    surfaceCard(new LastResolvedCardQuery(new SelfQuery<>()), 10)),
            new TargetingInfo(NONE)),
    HEAL(
            "Heal",
            "restore",
            "Restore 2 to self",
            10,
            selfHeal(2),
            new TargetingInfo(NONE)),
    TILL_SOIL(
            "Till Soil",
            "regenesis",
            "Till the current tile",
            20,
            editTile(SOIL),
            new TargetingInfo(HEXAGON, new RangeCondition(10))),
    PLANT_SEED(
            "Plant seed",
            "regenesis",
            "Plant a seed on current tile",
            20,
            buryAnySeed(),
            new TargetingInfo(NONE)),
    GATHER(
            "Gather",
            "gather",
            "Gather items on current tile",
            20,
            gather(1),
            new TargetingInfo(NONE)),
    CREATE_ROCK(
            "Create Rock",
            "meteor",
            "Create a rock on target tile",
            20,
            createStructure(StructureType.ROCK),
            new TargetingInfo(HEXAGON, new RangeCondition(1), new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
    ELECTROSTATIC_ZAPPER(
            "Electrostatic Zapper",
            "overclocked_machinery",
            "Whenever a card is played within range 5, deal 2 to the source",
            20,
            createStructure(StructureType.ELECTROSTATIC_ZAPPER),
            new TargetingInfo(HEXAGON, new RangeCondition(1), new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
    MELEE_ATTACK(
            "Melee Attack",
            "bash",
            "Deal 2 melee damage to target character",
            20,
            meleeDamage(2),
            new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
    WOODEN_CHEST(
            "Wooden Chest",
            "overclocked_machinery",
            "Create a chest on target tile",
            20,
            createStructure(StructureType.CHEST),
            new TargetingInfo(HEXAGON, new RangeCondition(1), new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>())))),
    FLAME_CIRCLE(
            "Flame Circle",
            "flame_circle",
            "Deal 4 damage to all enemies within radius 3",
            50,
            delayed(
                    and(
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
            and(),
            new TargetingInfo(NONE)),
    FREEZE(
            "Freeze",
            "ice_cube",
            "Add an Ice Cube to the target's stack.",
            20,
            addCardToStack(ICE_CUBE, new TargetQuery<>()),
            new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
    VENOMOUS_STRIKE(
            "Venomous Strike",
            "venomous_strike",
            "Deal 3 damage and apply 3 poison to target character",
            20,
            and(
                    damage(3),
                    applyStatus(new TargetQuery<>(), POISON, new LiteralQuery(3))
            ),
            new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
    PURGE_POISON(
            "Purge Poison",
            "purge_poison",
            "Remove up to 10 poison from target character and deal that much damage to it",
            25,
            and(
                    removeStatus(POISON,
                            new MinQuery(
                                    new StatusCountQuery(POISON, new TargetQuery<>()),
                                    new LiteralQuery(10)
                            )
                    ),
                    damage(
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
            damage(new RandomIntQuery(2, 4)),
            new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
    INVINCIBILITY(
            "Invincibility",
            "restore",
            "Gain 1 invincible",
            10,
            applyStatus(new SelfQuery<>(), INVINCIBLE, new LiteralQuery(1)),
            new TargetingInfo(NONE)),
    DOUBLE_STRIKE(
            "Double Strike",
            "big_punch",
            "Deal 2 damage, twice.",
            20,
            delayed(
                    and(
                            damage(2),
                            delayed(
                                    damage(2),
                                    5, 0
                            )
                    ),
                    2, 0),
            new TargetingInfo(CARD_PLAYER, new RangeCondition(1))),
    HEAVY_JUMP(
            "Heavy Jump",
            "heavy_jump",
            "Jump exactly 2 tiles away and deal 1 damage to all actors within 1 tile of landing point.",
            20,
            and(
                    move(10),
                    delayed(new DamageActorsExpression(new ActorsOnTilesQuery(new TilesInRadiusQuery(new TileQuery(new TargetQuery<>()), 1), true), 1), 0, 0)
            ),
            new TargetingInfo(HEXAGON,
                    new ExactRangeCondition(new SelfQuery<>(), new TargetQuery<>(), 2),
                    new EmptyCondition(new ActorsOnTilesQuery(new TargetQuery<>()))));

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
