package context.connect;

import static context.connect.PeerConnectData.MAX_RETRIES;
import static context.connect.PeerConnectData.TIMEOUT_MILLISECONDS;
import static event.network.NomadRealmsNetworkEvent.toPacket;

import java.net.InetAddress;
import java.net.UnknownHostException;

import context.GameContext;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;

public class PeerConnectLogic extends GameLogic {

	public static final PacketAddress PEER_ADDRESS;

	static {
		InetAddress peerIP = null;
		try {
			peerIP = InetAddress.getByName("192.168.0.35");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		PEER_ADDRESS = new PacketAddress(peerIP, 44000);
	}

	@Override
	protected void init() {
		addHandler(PeerConnectRequestEvent.class, event -> {
			PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent();
			context().sendPacket(toPacket(connectResponse, PEER_ADDRESS));
			System.out.println("Connected with " + PEER_ADDRESS + "!");
			transitionToGame();
		});
		addHandler(PeerConnectResponseEvent.class, event -> {
			PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent();
			context().sendPacket(toPacket(connectResponse, PEER_ADDRESS));
			System.out.println("Connected with " + PEER_ADDRESS + "!");
			transitionToGame();
		});
	}

	@Override
	public void update() {
		PeerConnectData data = (PeerConnectData) context().data();
		long time = System.currentTimeMillis();
		if (!data.isConnected()) {
			if (data.timesTried() >= MAX_RETRIES) {
				System.out.println("Failed to connect!");
			} else if (time - data.lastTriedTime() >= TIMEOUT_MILLISECONDS) {
				PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(0);
				context().sendPacket(toPacket(connectRequest, PEER_ADDRESS));
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
		NomadsGameLogic logic = new NomadsGameLogic(PEER_ADDRESS);
		NomadsGameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		context().transition(context);
	}

}
