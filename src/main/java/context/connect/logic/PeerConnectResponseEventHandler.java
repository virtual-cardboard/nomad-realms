package context.connect.logic;

import java.util.function.Consumer;

import context.connect.PeerConnectData;
import event.network.peerconnect.PeerConnectResponseEvent;

public class PeerConnectResponseEventHandler implements Consumer<PeerConnectResponseEvent> {

	private PeerConnectData data;
	private long nonce;

	public PeerConnectResponseEventHandler(PeerConnectData data, long nonce) {
		this.data = data;
		this.nonce = nonce;
	}

	@Override
	public void accept(PeerConnectResponseEvent event) {
		if (event.nonce() == nonce) {
			System.out.println("Connected with " + event.source().address() + "!");
			data.setConnected();
		}
	}

}
