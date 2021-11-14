
package context.game.logic;

import static common.event.NetworkEvent.toPacket;
import static context.connect.PeerConnectLogic.PEER_ADDRESS;

import java.util.function.Consumer;

import context.GameContext;
import event.game.CardHoveredEvent;

public class CardHoveredEventHandler implements Consumer<CardHoveredEvent> {

	private GameContext context;

	public CardHoveredEventHandler(GameContext context) {
		this.context = context;
	}

	@Override
	public void accept(CardHoveredEvent event) {
		context.sendPacket(toPacket(event.toNetworkEvent(), PEER_ADDRESS));
	}

}
