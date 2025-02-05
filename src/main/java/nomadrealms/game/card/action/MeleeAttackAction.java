package nomadrealms.game.card.action;

import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.intent.DamageIntent;
import nomadrealms.game.world.World;

public class MeleeAttackAction implements Action {

    private final CardPlayer source;
    private final CardPlayer target;
    private boolean isComplete;

    public MeleeAttackAction(CardPlayer source, CardPlayer target) {
        this.source = source;
        this.target = target;
        this.isComplete = false;
    }

    @Override
    public void update(World world) {
        if (!isComplete) {
            new DamageIntent(target, source, 2).resolve(world);
            isComplete = true;
        }
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public int preDelay() {
        return 0;
    }

    @Override
    public int postDelay() {
        return 5;
    }
}
