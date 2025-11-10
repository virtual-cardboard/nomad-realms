package nomadrealms.context.game.card.intent;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.MeleeAttackAction;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class MeleeDamageIntent implements Intent {

	private final Actor target;
	private final Target source;
	private final int amount;

	public MeleeDamageIntent(Target target, Target source, int amount) {
		this.target = (Actor) target;
		this.source = source;
		this.amount = amount;
	}

	@Override
	public void resolve(World world) {
		if (source instanceof CardPlayer) {
			((CardPlayer) source).queueAction(new MeleeAttackAction((CardPlayer) source, target, amount));
		} else {
			target.damage(amount);
		}
	}

}
