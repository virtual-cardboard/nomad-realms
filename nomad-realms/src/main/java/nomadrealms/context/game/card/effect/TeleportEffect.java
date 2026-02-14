package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class TeleportEffect extends Effect {

	private final Tile target;
	private final int delay;

	public TeleportEffect(Actor source, Tile target, int delay) {
		this.source = source;
		this.target = target;
		this.delay = delay;
	}

	@Override
	public void resolve(World world) {
		source.move(target);
	}

}
