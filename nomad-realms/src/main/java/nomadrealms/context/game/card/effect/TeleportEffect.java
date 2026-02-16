package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;

public class TeleportEffect extends Effect {

	private final CardPlayer source;
	private final Tile target;
	int delay = 0;

	public TeleportEffect(CardPlayer source, Tile target, int delay) {
		this.source = source;
		this.target = target;
		this.delay = delay;
		source(source);
	}

	@Override
	public void resolve(World world) {
		source.move(target);
	}

}
