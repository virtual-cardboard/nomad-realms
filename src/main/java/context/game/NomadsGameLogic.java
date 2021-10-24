package context.game;

import static common.event.NetworkEvent.toPacket;

import common.event.GameEvent;
import context.connect.PeerConnectLogic;
import context.connect.PeerConnectRequestEvent;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.network.CardPlayedNetworkEvent;

public class NomadsGameLogic extends GameLogic {

	public NomadsGameLogic(PacketAddress peerAddress) {
	}

	@Override
	protected void init() {
	}

	@Override
	public void update() {
		while (!eventQueue().isEmpty()) {
			GameEvent event = eventQueue().poll();
			if (event instanceof PeerConnectRequestEvent) {
				PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(0, null);
				context().sendPacket(toPacket(connectRequest, PeerConnectLogic.PEER_ADDRESS));
			} else if (event instanceof CardPlayedNetworkEvent) {
				CardPlayedNetworkEvent cardPlayed = (CardPlayedNetworkEvent) event;
				cardPlayed.card();
				cardPlayed.target();
			}
		}
	}

}
