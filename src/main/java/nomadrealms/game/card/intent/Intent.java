package nomadrealms.game.card.intent;

import nomadrealms.game.GameState;

public interface Intent {

	public void resolve(GameState gameState);

}
