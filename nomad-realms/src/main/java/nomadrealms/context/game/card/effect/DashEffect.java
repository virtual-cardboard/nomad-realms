package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.action.DashAction;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class DashEffect extends Effect {

	private final Tile target;
	int duration = 0;

	public DashEffect(Actor source, Tile target, int duration) {
		super(source);
		this.target = target;
		this.duration = duration;
	}

	@Override
	public void resolve(World world) {
		source().queueAction(new DashAction(source(), target, duration));
	}

}
