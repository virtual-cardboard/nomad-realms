package context.connect;

import static context.connect.PeerConnectData.MAX_RETRIES;
import static context.connect.PeerConnectData.TIMEOUT_MILLISECONDS;

import java.util.ArrayDeque;
import java.util.Queue;

import context.GameContext;
import context.audio.GameAudio;
import context.connect.logic.PeerConnectRequestEventHandler;
import context.connect.logic.PeerConnectResponseEventHandler;
import context.data.GameData;
import context.game.NomadsGameAudio;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.input.GameInput;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import engine.common.event.GameEvent;
import event.network.bootstrap.BootstrapResponseEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import event.network.peerconnect.PeerConnectResponseEvent;

public class PeerConnectLogic extends GameLogic {

	private PacketAddress lanAddress;
	private PacketAddress wanAddress;
	private long nonce;

	private PeerConnectData data;

	private Queue<PacketModel> networkSync = new ArrayDeque<>();

	public PeerConnectLogic(BootstrapResponseEvent response) {
		if (response != null) {
			lanAddress = response.lanAddress();
			wanAddress = response.wanAddress();
			nonce = response.nonce();
		}
	}

	@Override
	protected void init() {
		data = (PeerConnectData) context().data();
		addHandler(PeerConnectRequestEvent.class, new PeerConnectRequestEventHandler(data, nonce, networkSync));
		addHandler(PeerConnectResponseEvent.class, new PeerConnectResponseEventHandler(data, nonce, networkSync));
		addHandler(GameEvent.class, event -> System.out.println("Received " + event.getClass().getSimpleName()));
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
			PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(nonce, data.username());
			context().sendPacket(connectRequest.toPacket(lanAddress));
			System.out.println(lanAddress);
			System.out.println(wanAddress);
			data.setLastTriedTime(time);
			data.incrementTimesTried();
			System.out.println("Trying to connect...");
			System.out.println(context().socketPort() & 0xFFFF);
		}
	}

	private void transitionToGame() {
		GameAudio audio = new NomadsGameAudio();
		GameData data = new NomadsGameData();
		GameInput input = new NomadsGameInput();
		GameLogic logic = new NomadsGameLogic(this.data.getPeerAddress(), nonce, this.data.username());
		GameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(audio, data, input, logic, visuals);
		context().transition(context);
	}

}
