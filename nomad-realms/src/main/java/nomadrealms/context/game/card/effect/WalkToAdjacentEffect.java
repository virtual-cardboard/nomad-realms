package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.WalkToAdjacentAction;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class WalkToAdjacentEffect extends Effect {

	private final Target target;
	private final int delay;

	public WalkToAdjacentEffect(CardPlayer source, Target target, int delay) {
		super(source);
		this.target = target;
		this.delay = delay;
	}

	@Override
	public void resolve(World world) {
		((CardPlayer) source()).queueAction(new WalkToAdjacentAction((CardPlayer) source(), target, delay));
	}

}
