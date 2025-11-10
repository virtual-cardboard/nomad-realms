package nomadrealms.context.game.card.intent;

import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.action.MoveAction;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class MoveIntent implements Intent {

	private final CardPlayer source;
	private final Tile target;
	int delay = 0;

	public MoveIntent(CardPlayer source, Tile target, int delay) {
		this.source = source;
		this.target = target;
		this.delay = delay;
	}

	@Override
	public void resolve(World world) {
		source.queueAction(new MoveAction(source, target, delay));
	}

}
