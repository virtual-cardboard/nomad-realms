package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.action.MoveAction;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class MoveEffect extends Effect {

	private final Tile target;
	int delay = 0;

	public MoveEffect(Actor source, Tile target, int delay) {
		super(source);
		this.target = target;
		this.delay = delay;
	}

	@Override
	public void resolve(World world) {
		source().queueAction(new MoveAction(source(), target, delay));
	}

	@Override
	public Target target() {
		return target;
	}

}
