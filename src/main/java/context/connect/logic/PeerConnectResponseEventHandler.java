package context.connect.logic;

import static java.lang.System.currentTimeMillis;

import java.util.Queue;
import java.util.function.Consumer;

import context.connect.PeerConnectData;
import engine.common.networking.packet.PacketModel;
import event.network.p2p.peerconnect.PeerConnectResponseEvent;

public class PeerConnectResponseEventHandler implements Consumer<PeerConnectResponseEvent> {

	private final PeerConnectData data;
	private final Queue<PacketModel> networkSync;

	public PeerConnectResponseEventHandler(PeerConnectData data, Queue<PacketModel> networkSync) {
		this.data = data;
		this.networkSync = networkSync;
	}

	@Override
	public void accept(PeerConnectResponseEvent event) {
		if (event.nonce() == data.nonce()) {
			System.out.println("Connected with " + event.source().address() + "!");
			PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent(currentTimeMillis(), data.nonce(), data.username());
			networkSync.add(connectResponse.toPacketModel(event.source().address()));
			data.setConnected();
			data.setPeerAddress(event.source().address());
		}
	}

}
