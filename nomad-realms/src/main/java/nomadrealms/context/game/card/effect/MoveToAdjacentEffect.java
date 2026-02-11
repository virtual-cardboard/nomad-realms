package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.MoveToAdjacentAction;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class MoveToAdjacentEffect extends Effect {

	private final CardPlayer source;
	private final Target target;
	private final int delay;

	public MoveToAdjacentEffect(CardPlayer source, Target target, int delay) {
		this.source = source;
		this.target = target;
		this.delay = delay;
	}

	@Override
	public void resolve(World world) {
		source.queueAction(new MoveToAdjacentAction(source, target, delay));
	}

}
