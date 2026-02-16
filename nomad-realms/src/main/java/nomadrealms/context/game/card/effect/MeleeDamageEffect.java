package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.MeleeAttackAction;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class MeleeDamageEffect extends Effect {

	private final Actor target;
	private final Target source;
	private final int amount;

	public MeleeDamageEffect(Target target, Target source, int amount) {
		this.target = (Actor) target;
		this.source = source;
		this.amount = amount;
		if (source instanceof Actor) {
			source((Actor) source);
		}
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
