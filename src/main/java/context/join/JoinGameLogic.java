package context.join;

import static networking.ClientNetworkUtils.LOCAL_HOST;
import static networking.ClientNetworkUtils.SERVER;

import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.network.join.JoinClusterRequestEvent;
import event.network.join.JoinClusterResponseEvent;

public final class JoinGameLogic extends GameLogic {

	private JoinGameData data;

	@Override
	protected void init() {
		data = (JoinGameData) context().data();
		System.out.println("Sending JoinWorldRequestEvent to server");
		JoinClusterRequestEvent joinWorldRequestEvent = new JoinClusterRequestEvent(0, LOCAL_HOST.address(), data.username());
		PacketAddress serverAddress = SERVER.address();
		context().sendPacket(joinWorldRequestEvent.toPacket(serverAddress));

		addHandler(JoinClusterResponseEvent.class, event -> {
			// TODO
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
