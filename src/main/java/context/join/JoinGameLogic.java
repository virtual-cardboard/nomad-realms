package context.join;

import static networking.ClientNetworkUtils.LOCAL_HOST;
import static networking.ClientNetworkUtils.SERVER;

import context.GameContext;
import context.audio.GameAudio;
import context.data.GameData;
import context.game.NomadsGameAudio;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.input.GameInput;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import event.network.NomadRealmsNetworkEvent;
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
		addHandler(NomadRealmsNetworkEvent.class, event -> System.out.println(event.getClass().getSimpleName()));
		addHandler(JoinEmptyClusterResponseEvent.class, event -> {
			GameAudio audio = new NomadsGameAudio();
			GameData data = new NomadsGameData();
			GameInput input = new NomadsGameInput();
			GameLogic logic = new NomadsGameLogic(this.data.username());
			GameVisuals visuals = new NomadsGameVisuals();
			GameContext context = new GameContext(audio, data, input, logic, visuals);
			context().transition(context);
		});
		addHandler(JoinClusterResponseEvent.class, event -> {
		});
	}

	@Override
	public void update() {
	}

//	private void transitionToPeerConnect() {
//		GameData data = new PeerConnectData(this.data.username());
//		GameInput input = new PeerConnectInput();
//		GameLogic logic = new PeerConnectLogic(this.data.response());
//		GameVisuals visuals = new PeerConnectVisuals();
//		GameContext context = new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
//		context().transition(context);
//	}

}
