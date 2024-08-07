package nomadrealms.game.card.intent;

import nomadrealms.game.event.Target;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.actor.HasHealth;
import nomadrealms.game.world.World;

public class HealIntent implements Intent {

	private final Target target;
	private final CardPlayer source;
	private final int amount;

	public HealIntent(Target target, CardPlayer source, int amount) {
		this.target = target;
		this.source = source;
		this.amount = amount;
	}

	@Override
	public void resolve(World world) {
		((HasHealth) target).heal(amount);
	}

}
