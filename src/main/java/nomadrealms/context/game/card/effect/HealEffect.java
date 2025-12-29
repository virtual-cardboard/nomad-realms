package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.HasHealth;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class HealEffect extends Effect {

	private final Target target;
	private final CardPlayer source;
	private final int amount;

	public HealEffect(Target target, CardPlayer source, int amount) {
		this.target = target;
		this.source = source;
		this.amount = amount;
	}

	@Override
	public void resolve(World world) {
		((HasHealth) target).heal(amount);
	}

}
