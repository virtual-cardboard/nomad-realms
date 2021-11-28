package context.connect;

import static context.connect.PeerConnectData.MAX_RETRIES;
import static context.connect.PeerConnectData.TIMEOUT_MILLISECONDS;
import static event.network.NomadRealmsNetworkEvent.toPacket;

import context.GameContext;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.connect.BootstrapResponseEvent;

public class PeerConnectLogic extends GameLogic {

	private PacketAddress lanAddress;
	private PacketAddress wanAddress;
	private long nonce;

	private PeerConnectData data;

	public PeerConnectLogic(BootstrapResponseEvent response) {
		lanAddress = response.lanAddress();
		wanAddress = response.wanAddress();
		nonce = response.nonce();

	}

	@Override
	protected void init() {
		data = (PeerConnectData) context().data();
		addHandler(PeerConnectRequestEvent.class, event -> {
			PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent();
			context().sendPacket(toPacket(connectResponse, lanAddress));
			System.out.println("Connected with " + lanAddress + "!");
			data.setConnected();
			// If we directly call transitionToGame(), then there is a chance we transition
			// twice: once in this PeerConnectRequestRequestEvent handler and once in the
			// PeerConnectResponseEvent handler. Instead, we use a flag.
		});
		addHandler(PeerConnectResponseEvent.class, event -> {
			PeerConnectResponseEvent connectResponse = new PeerConnectResponseEvent();
			context().sendPacket(toPacket(connectResponse, lanAddress));
			System.out.println("Connected with " + lanAddress + "!");
			data.setConnected();
		});
	}

	@Override
	public void update() {
		if (data.isConnected()) {
			transitionToGame();
			return;
		}
		long time = System.currentTimeMillis();
		if (data.timesTried() >= MAX_RETRIES) {
			System.out.println("Failed to connect!");
		} else if (time - data.lastTriedTime() >= TIMEOUT_MILLISECONDS) {
			PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(0);
			context().sendPacket(toPacket(connectRequest, lanAddress));
			context().sendPacket(toPacket(connectRequest, wanAddress));
			data.setLastTriedTime(time);
			data.incrementTimesTried();
			System.out.println("Trying to connect...");
			System.out.println(context().socketPort() & 0xFFFF);
		}
	}

	private void transitionToGame() {
		NomadsGameData data = new NomadsGameData();
		NomadsGameInput input = new NomadsGameInput();
		NomadsGameLogic logic = new NomadsGameLogic(lanAddress);
		NomadsGameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		context().transition(context);
	}

}
