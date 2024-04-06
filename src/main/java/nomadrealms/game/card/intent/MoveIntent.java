package nomadrealms.game.card.intent;

import nomadrealms.game.GameState;
import nomadrealms.game.event.IntentEmitter;
import nomadrealms.game.world.actor.CardPlayer;
import nomadrealms.game.world.actor.HasPosition;
import nomadrealms.game.world.map.tile.Tile;

public class MoveIntent implements Intent {

	private final HasPosition source;
	private Tile target;

	public MoveIntent(HasPosition source, Tile target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public void resolve(GameState gameState) {
		source.move(target);
	}

}
