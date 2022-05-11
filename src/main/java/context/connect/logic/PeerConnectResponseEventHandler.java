package context.connect.logic;

import java.util.Queue;
import java.util.function.Consumer;

import context.connect.PeerConnectData;
import context.input.networking.packet.PacketModel;
import event.network.peerconnect.PeerConnectResponseEvent;

public class PeerConnectResponseEventHandler implements Consumer<PeerConnectResponseEvent> {

	private PeerConnectData data;
	private long nonce;
	private Queue<PacketModel> networkSync;

	public PeerConnectResponseEventHandler(PeerConnectData data, long nonce, Queue<PacketModel> networkSync) {
		this.data = data;
		this.nonce = nonce;
		this.networkSync = networkSync;
	}

	@Override
	public void accept(PeerConnectResponseEvent event) {
		if (event.nonce() == nonce) {
			System.out.println("Connected with " + event.source().address() + "!");
			PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent(System.currentTimeMillis(), nonce, data.username());
			networkSync.add(connectResponse.toPacketModel(event.source().address()));
			data.setConnected();
			data.setPeerAddress(event.source().address());
		}
	}

}
