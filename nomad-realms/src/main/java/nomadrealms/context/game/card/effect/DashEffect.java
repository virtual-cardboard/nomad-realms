package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.DashAction;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import engine.serialization.Derializable;

@Derializable
public class DashEffect extends Effect {

	private final Tile target;
	int duration = 0;

	public DashEffect(CardPlayer source, Tile target, int duration) {
		super(source);
		this.target = target;
		this.duration = duration;
	}

	@Override
	public void resolve(World world) {
		((CardPlayer) source()).queueAction(new DashAction((CardPlayer) source(), target, duration));
	}

	/**
	 * No-arg constructor for serialization.
	 */
	DashEffect() {
		this.target = null;
	}
}

