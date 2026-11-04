package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import engine.serialization.Derializable;

@Derializable
public class MoveEffect extends Effect {

	private final Tile target;

	public MoveEffect(Actor source, Tile target) {
		super(source);
		this.target = target;
	}

	@Override
	public void resolve(World world) {
		source().move(target);
	}

	/**
	 * No-arg constructor for serialization.
	 */
	MoveEffect() {
		this.target = null;
	}
}

