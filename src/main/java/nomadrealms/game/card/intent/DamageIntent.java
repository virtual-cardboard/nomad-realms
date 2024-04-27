package nomadrealms.game.card.intent;

import nomadrealms.game.event.Target;
import nomadrealms.game.world.actor.CardPlayer;
import nomadrealms.game.world.actor.HasHealth;
import nomadrealms.game.world.map.World;

public class DamageIntent implements Intent {

	private final Target target;
	private final CardPlayer source;
	private final int amount;

	public DamageIntent(Target target, CardPlayer source, int amount) {
		this.target = target;
		this.source = source;
		this.amount = amount;
	}

	@Override
	public void resolve(World world) {
		((HasHealth) target).damage(amount);
	}

}
