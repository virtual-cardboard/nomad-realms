package nomadrealms.game.card;

import nomadrealms.game.card.expression.CardExpression;
import nomadrealms.game.card.expression.DamageExpression;
import nomadrealms.game.card.expression.MoveExpression;
import nomadrealms.game.card.expression.SelfHealExpression;
import nomadrealms.game.card.target.TargetingInfo;

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
