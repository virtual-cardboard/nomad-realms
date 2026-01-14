package nomadrealms.context.game.card.query;

import java.util.List;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class LiteralQuery implements Query<Integer> {

    private final int value;

    public LiteralQuery(int value) {
        this.value = value;
    }

    @Override
    public List<Integer> find(World world, CardPlayer source, Target target) {
        return List.of(value);
    }
}
