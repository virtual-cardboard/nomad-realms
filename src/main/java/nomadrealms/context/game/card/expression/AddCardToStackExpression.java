package nomadrealms.context.game.card.expression;

import java.util.List;
import java.util.stream.Collectors;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.effect.AddCardToStackEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class AddCardToStackExpression implements CardExpression {

    private final GameCard card;
    private final Query<CardPlayer> targets;

    public AddCardToStackExpression(GameCard card, Query<CardPlayer> targets) {
        this.card = card;
        this.targets = targets;
    }

    @Override
    public List<Effect> effects(World world, Target target, CardPlayer source) {
        return targets.find(world, source, target).stream()
                .map(t -> new AddCardToStackEffect(t, card))
                .collect(Collectors.toList());
    }

}
