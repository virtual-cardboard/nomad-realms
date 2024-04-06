package nomadrealms.game.card.block;

import nomadrealms.game.GameState;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.actor.CardPlayer;

public interface Intent {

	public void resolve(GameState gameState);

}
