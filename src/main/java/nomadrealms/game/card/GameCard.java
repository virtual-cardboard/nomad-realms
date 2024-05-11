package nomadrealms.game.card;

import nomadrealms.game.card.expression.*;
import nomadrealms.game.card.target.TargetingInfo;
import nomadrealms.game.world.map.tile.factory.TileType;

import static nomadrealms.game.card.target.TargetType.*;

public class GameCard implements Card {

    public static final GameCard MOVE = new GameCard(
            "Move",
            "Move to target hexagon",
            new MoveExpression(),
            new TargetingInfo(HEXAGON, 10));
    public static final GameCard ATTACK = new GameCard(
            "Attack",
            "Deal 2 to target character",
            new DamageExpression(2),
            new TargetingInfo(CARD_PLAYER, 10));
    public static final GameCard HEAL = new GameCard(
            "Heal",
            "Restore 2 to self",
            new SelfHealExpression(2),
            new TargetingInfo(NONE, 10));
    public static final GameCard TILL_SOIL = new GameCard(
            "Till Soil",
            "Till the current tile",
            new EditTileExpression(TileType.SOIL),
            new TargetingInfo(HEXAGON, 10));
    public static final GameCard PLANT_SEED = new GameCard(
            "Plant seed",
            "Plant a seed on current tile",
            new BuryAnySeedExpression(),
            new TargetingInfo(NONE, 10));
    public static final GameCard GATHER = new GameCard(
            "Gather",
            "Gather items on current tile",
            new GatherExpression(1),
            new TargetingInfo(NONE, 1));

    private final String name;
    private final String description;
    private final CardExpression expression;
    private final TargetingInfo targetingInfo;

    public GameCard(String name, String description, CardExpression expression, TargetingInfo targetingInfo) {
        this.name = name;
        this.description = description;
        this.expression = expression;
        this.targetingInfo = targetingInfo;
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

    public TargetingInfo targetingInfo() {
        return targetingInfo;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name +
                '}';
    }
}
