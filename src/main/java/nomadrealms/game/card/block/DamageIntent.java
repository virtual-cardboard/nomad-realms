package nomadrealms.game.card.block;

import nomadrealms.game.GameState;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.actor.CardPlayer;
import nomadrealms.game.world.actor.HasHealth;

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
	public void resolve(GameState gameState) {
		((HasHealth) target).damage(amount);
	}

}
