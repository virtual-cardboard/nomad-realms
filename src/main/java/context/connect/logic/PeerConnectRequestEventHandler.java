package context.connect.logic;

import static event.network.NomadRealmsNetworkEvent.toPacket;

import java.util.Queue;
import java.util.function.Consumer;

import context.connect.PeerConnectData;
import context.input.networking.packet.PacketModel;
import event.network.peerconnect.PeerConnectRequestEvent;
import event.network.peerconnect.PeerConnectResponseEvent;

public class PeerConnectRequestEventHandler implements Consumer<PeerConnectRequestEvent> {

	private PeerConnectData data;
	private long nonce;
	private Queue<PacketModel> networkSync;

	public PeerConnectRequestEventHandler(PeerConnectData data, long nonce, Queue<PacketModel> networkSync) {
		this.data = data;
		this.nonce = nonce;
		this.networkSync = networkSync;
	}

	@Override
	public void accept(PeerConnectRequestEvent event) {
		if (event.nonce() == nonce) {
			PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent(nonce, data.username());
			networkSync.add(toPacket(connectResponse, event.source().address()));
			System.out.println("Connected with " + event.source().address() + "!");
			data.setConnected();
		}
	}

}