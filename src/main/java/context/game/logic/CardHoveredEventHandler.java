
package context.game.logic;

import java.util.function.Consumer;

import context.GameContext;
import event.game.playerinput.PlayerHoveredCardEvent;

public class CardHoveredEventHandler implements Consumer<PlayerHoveredCardEvent> {

	private GameContext context;

	public CardHoveredEventHandler(GameContext context) {
		this.context = context;
	}

	@Override
	public void accept(PlayerHoveredCardEvent event) {
//		context.sendPacket(toPacket(event.toNetworkEvent(), PEER_ADDRESS));
	}

}
