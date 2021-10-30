package context.game;

import static common.event.NetworkEvent.toPacket;
import static context.connect.PeerConnectLogic.PEER_ADDRESS;

import common.event.GameEvent;
import context.connect.PeerConnectRequestEvent;
import context.connect.PeerConnectResponseEvent;
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
				PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent(null);
				context().sendPacket(toPacket(connectResponse, PEER_ADDRESS));
				System.out.println(PEER_ADDRESS + " joined late");
			} else if (event instanceof CardPlayedNetworkEvent) {
				CardPlayedNetworkEvent cardPlayed = (CardPlayedNetworkEvent) event;
				cardPlayed.card();
				cardPlayed.target();
			}
		}
	}

}
