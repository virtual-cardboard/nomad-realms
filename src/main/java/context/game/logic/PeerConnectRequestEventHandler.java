package context.game.logic;

import static common.event.NetworkEvent.toPacket;
import static context.connect.PeerConnectLogic.PEER_ADDRESS;

import java.util.function.Consumer;

import context.GameContext;
import context.connect.PeerConnectRequestEvent;
import context.connect.PeerConnectResponseEvent;
import context.input.networking.packet.PacketModel;

public class PeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private GameContext context;

	public PeerConnectRequestEventHandler(GameContext context) {
		this.context = context;
	}

	@Override
	public void accept(PeerConnectRequestEvent t) {
		PeerConnectResponseEvent event = new PeerConnectResponseEvent();
		PacketModel packet = toPacket(event, PEER_ADDRESS);
		context.sendPacket(packet);
		System.out.println(PEER_ADDRESS + " joined late");
	}

}
