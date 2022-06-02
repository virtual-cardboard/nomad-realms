package context.connect.logic;

import java.util.Queue;
import java.util.function.Consumer;

import context.connect.PeerConnectData;
import engine.common.networking.packet.PacketModel;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.peerconnect.PeerConnectResponseEvent;

public class PeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private final PeerConnectData data;
	private final Queue<PacketModel> networkSend;

	public PeerConnectRequestEventHandler(PeerConnectData data, Queue<PacketModel> networkSend) {
		this.data = data;
		this.networkSend = networkSend;
	}

	@Override
	public void accept(PeerConnectRequestEvent event) {
		if (event.nonce() == data.nonce()) {
			PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent(data.nonce(), data.username());
			networkSend.add(connectResponse.toPacketModel(event.source().address()));
			System.out.println("Connected with " + event.source().address() + "!");
			data.confirmConnectedPeer(event.source().address());
		}
	}

}
