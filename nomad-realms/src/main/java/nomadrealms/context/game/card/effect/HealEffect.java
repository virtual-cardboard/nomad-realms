package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.HasHealth;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class HealEffect extends Effect {

	private final Target target;
	private final int amount;

	public HealEffect(Target target, CardPlayer source, int amount) {
		super(source);
		this.target = target;
		this.amount = amount;
	}

	@Override
	public void resolve(World world) {
		((HasHealth) target).heal(amount);
	}

}
