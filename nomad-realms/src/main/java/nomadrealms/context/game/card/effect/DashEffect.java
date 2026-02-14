package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.action.DashAction;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class DashEffect extends Effect {

	private final Tile target;
	private final int duration;

	public DashEffect(Actor source, Tile target, int duration) {
		this.source = source;
		this.target = target;
		this.duration = duration;
	}

	@Override
	public void resolve(World world) {
		source.queueAction(new DashAction(source, target, duration));
	}

}
