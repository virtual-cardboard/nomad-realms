package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.WalkAction;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class WalkEffect extends Effect {

	private final Tile target;
	int delay = 0;

	public WalkEffect(CardPlayer source, Tile target, int delay) {
		super(source);
		this.target = target;
		this.delay = delay;
	}

	@Override
	public void resolve(World world) {
		((CardPlayer) source()).queueAction(new WalkAction((CardPlayer) source(), target, delay));
	}

}
