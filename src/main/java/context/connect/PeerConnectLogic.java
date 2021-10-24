package context.connect;

import static common.event.NetworkEvent.toPacket;
import static context.connect.PeerConnectData.MAX_RETRIES;
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

	private static final PacketAddress peerAddress;

	static {
		InetAddress peerIP = null;
		try {
			peerIP = InetAddress.getByName("192.168.0.28");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		peerAddress = new PacketAddress(peerIP, 44000);
	}

	@Override
	public void update() {
		while (!eventQueue().isEmpty()) {
			GameEvent poll = eventQueue().poll();
			if (poll instanceof PeerConnectRequestEvent) {
				PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(0, null);
				context().sendPacket(toPacket(connectRequest, peerAddress));
				System.out.println("Connected with " + peerAddress + "!");
				transitionToGame();
			}
		}
		PeerConnectData data = (PeerConnectData) context().data();
		long time = System.currentTimeMillis();
		if (!data.isConnected()) {
			if (data.timesTried() >= MAX_RETRIES) {
				System.out.println("Failed to connect!");
			} else if (time - data.lastTriedTime() >= TIMEOUT_MILLISECONDS) {
				PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(0, null);
				context().sendPacket(toPacket(connectRequest, peerAddress));
				data.setLastTriedTime(time);
				data.incrementTimesTried();
				System.out.println("Trying to connect...");
				System.out.println(context().socketPort() & 0xFFFF);
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
