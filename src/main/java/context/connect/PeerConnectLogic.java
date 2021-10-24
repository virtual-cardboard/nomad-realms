package context.connect;

import static common.event.NetworkEvent.toPacket;
import static context.connect.PeerConnectData.TIMEOUT_MILLISECONDS;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.event.GameEvent;
import context.GameContext;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;

public class PeerConnectLogic extends GameLogic {

	private InetAddress peerIP;
	private PacketAddress peerAddress;

	public PeerConnectLogic() {
		try {
			peerIP = InetAddress.getByName("196.168.0.35");
			peerAddress = new PacketAddress(peerIP, 44000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		while (!eventQueue().isEmpty()) {
			GameEvent poll = eventQueue().poll();
			if (poll instanceof PeerConnectRequestEvent) {
				PeerConnectRequestEvent connectRequest = (PeerConnectRequestEvent) poll;
				System.out.println(connectRequest.toString());
				transitionToGame();
			}
		}
		PeerConnectData data = (PeerConnectData) context().data();
		long time = System.currentTimeMillis();
		if (!data.isConnected()) {
			if (data.timesTried() >= data.timesTried()) {
				System.out.println("Failed to connect!");
			} else if (time - data.lastTriedTime() >= TIMEOUT_MILLISECONDS) {
				PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(0, null);
				context().sendPacket(toPacket(connectRequest, peerAddress));
				data.setLastTriedTime(time);
				data.incrementTimesTried();
				System.out.println("Trying to connect...");
			}
		}
		if (data.isConnected()) {
			transitionToGame();
		}
	}

	private void transitionToGame() {
		NomadsGameData data = new NomadsGameData();
		NomadsGameInput input = new NomadsGameInput();
		NomadsGameLogic logic = new NomadsGameLogic(peerAddress);
		NomadsGameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		context().transition(context);
	}

}
