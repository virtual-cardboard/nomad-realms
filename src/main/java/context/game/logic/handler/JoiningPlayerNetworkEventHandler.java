package context.game.logic.handler;

import static java.lang.System.currentTimeMillis;

import java.util.Queue;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import engine.common.networking.packet.PacketModel;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.s2c.JoiningPlayerNetworkEvent;

public class JoiningPlayerNetworkEventHandler implements Consumer<JoiningPlayerNetworkEvent> {

	private final NomadsGameData data;
	private final Queue<PacketModel> networkSend;

	public JoiningPlayerNetworkEventHandler(NomadsGameData data, Queue<PacketModel> networkSend) {
		this.data = data;
		this.networkSend = networkSend;
	}

	@Override
	public void accept(JoiningPlayerNetworkEvent e) {
		PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(currentTimeMillis(), e.nonce(), data.username());
		networkSend.add(connectRequest.toPacketModel(e.lanAddress()));
		networkSend.add(connectRequest.toPacketModel(e.wanAddress()));
		data.setJoiningPlayerNonce(e.nonce());
	}

}
