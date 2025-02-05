package nomadrealms.game.card.intent;

import nomadrealms.game.actor.HasHealth;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.action.MeleeAttackAction;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.World;

public class MeleeDamageIntent implements Intent {

    private final Target target;
    private final Target source;
    private final int amount;

    public MeleeDamageIntent(Target target, Target source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
    }

    @Override
    public void resolve(World world) {
        ((HasHealth) target).damage(amount);
        if (source instanceof CardPlayer && target instanceof CardPlayer) {
            ((CardPlayer) source).queueAction(new MeleeAttackAction((CardPlayer) source, (CardPlayer) target));
        }
    }
}
