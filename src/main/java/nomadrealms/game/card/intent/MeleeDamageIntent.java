package nomadrealms.game.card.intent;

import nomadrealms.game.actor.Actor;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.action.MeleeAttackAction;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.World;

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
