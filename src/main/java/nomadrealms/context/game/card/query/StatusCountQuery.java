package nomadrealms.context.game.card.query;

import java.util.List;
import nomadrealms.context.game.actor.status.StatusEffect;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class StatusCountQuery implements Query<Integer> {

    private final StatusEffect statusEffect;
    private final Query<CardPlayer> target;

    public StatusCountQuery(StatusEffect statusEffect, Query<CardPlayer> target) {
        this.statusEffect = statusEffect;
        this.target = target;
    }

    @Override
    public List<Integer> find(World world, CardPlayer source, Target t) {
        return target.find(world, source, t).stream()
                .map(cardPlayer -> cardPlayer.status().count(statusEffect))
                .toList();
    }
}
