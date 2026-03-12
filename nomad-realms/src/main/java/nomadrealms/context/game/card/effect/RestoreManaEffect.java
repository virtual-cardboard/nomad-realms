package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;

public class RestoreManaEffect extends Effect {

	private final CardPlayer target;
	private final int amount;

	public RestoreManaEffect(Actor source, CardPlayer target, int amount) {
		super(source);
		this.target = target;
		this.amount = amount;
	}

	@Override
	public void resolve(World world) {
		target.mana(target.mana() + amount);
	}

}
