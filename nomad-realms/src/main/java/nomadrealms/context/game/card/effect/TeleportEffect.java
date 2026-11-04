package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import engine.serialization.Derializable;

@Derializable
public class TeleportEffect extends Effect {

	private final Tile target;
	int delay = 0;

	public TeleportEffect(CardPlayer source, Tile target, int delay) {
		super(source);
		this.target = target;
		this.delay = delay;
	}

	@Override
	public void resolve(World world) {
		source().move(target);
	}

	/**
	 * No-arg constructor for serialization.
	 */
	TeleportEffect() {
		this.target = null;
	}
}

