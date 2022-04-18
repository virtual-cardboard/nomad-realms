package context.join;

import static networking.ClientNetworkUtils.LOCAL_HOST;
import static networking.ClientNetworkUtils.SERVER;

import context.GameContext;
import context.audio.GameAudio;
import context.data.GameData;
import context.game.*;
import context.input.GameInput;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import event.network.NomadRealmsP2PNetworkEvent;
import event.network.join.JoinClusterRequestEvent;
import event.network.join.JoinClusterResponseEvent;
import event.network.join.JoinEmptyClusterResponseEvent;

public final class JoinGameLogic extends GameLogic {

	private JoinGameData data;

	@Override
	protected void init() {
		data = (JoinGameData) context().data();
		System.out.println("Sending JoinWorldRequestEvent to server");
		JoinClusterRequestEvent joinWorldRequestEvent = new JoinClusterRequestEvent(0, LOCAL_HOST.address(), data.username());
		PacketAddress serverAddress = SERVER.address();
		context().sendPacket(joinWorldRequestEvent.toPacket(serverAddress));
		addHandler(JoinEmptyClusterResponseEvent.class, this::transitionToGame);
		addHandler(JoinClusterResponseEvent.class, this::transitionToBootstrap);

		addHandler(NomadRealmsP2PNetworkEvent.class, this::printEvent);
	}

	@Override
	public void update() {
	}

	private void transitionToGame(JoinEmptyClusterResponseEvent event) {
		System.out.println("Transitioning to game");
		GameAudio audio = new NomadsGameAudio();
		GameData data = new NomadsGameData();
		GameInput input = new NomadsGameInput();
		GameLogic logic = new NomadsGameLogic(this.data.username());
		GameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(audio, data, input, logic, visuals);
		context().transition(context);
	}

	private void transitionToBootstrap(JoinClusterResponseEvent event) {
		System.out.println("Transitioning to bootstrap");
	}

	private void printEvent(NomadRealmsP2PNetworkEvent event) {
		System.out.println("Received " + event.getClass().getSimpleName() + " from " + event.source().address());
	}

}
