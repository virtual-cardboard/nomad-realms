package context.join;

import static networking.ClientNetworkUtils.SERVER;

import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import event.network.join.JoinWorldRequestEvent;
import event.network.join.JoinWorldResponseEvent;

public final class JoinGameLogic extends GameLogic {

	private JoinGameData data;

	@Override
	protected void init() {
		data = (JoinGameData) context().data();
		System.out.println("Sending JoinWorldRequestEvent to server");
		JoinWorldRequestEvent joinWorldRequestEvent = new JoinWorldRequestEvent(0, data.username());
		PacketAddress serverAddress = SERVER.address();
		context().sendPacket(joinWorldRequestEvent.toPacket(serverAddress));

		addHandler(JoinWorldResponseEvent.class, event -> {
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
