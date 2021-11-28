package context.bootstrap;

import static networking.NetworkUtils.LOCAL_HOST;
import static networking.NetworkUtils.SERVER;

import context.GameContext;
import context.bootstrap.logic.BootstrapResponseEventHandler;
import context.connect.PeerConnectData;
import context.connect.PeerConnectInput;
import context.connect.PeerConnectLogic;
import context.connect.PeerConnectVisuals;
import context.data.GameData;
import context.input.GameInput;
import context.input.networking.packet.address.PacketAddress;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import event.network.bootstrap.BootstrapRequestEvent;
import event.network.bootstrap.BootstrapResponseEvent;

public final class BootstrapGameLogic extends GameLogic {

	private BootstrapGameData data;

	@Override
	protected void init() {
		data = (BootstrapGameData) context().data();
		addHandler(BootstrapResponseEvent.class, new BootstrapResponseEventHandler(data));
		BootstrapRequestEvent bootstrapRequestEvent = new BootstrapRequestEvent(LOCAL_HOST.address(), data.username());
		PacketAddress serverAddress = SERVER.address();
		System.out.println("Sending BootstrapRequestEvent to server at " + serverAddress);
		context().sendPacket(bootstrapRequestEvent.toPacket(serverAddress));
	}

	@Override
	public void update() {
		if (data.matched()) {
			transitionToPeerConnect();
		}
	}

	protected GameContext transitionToPeerConnect() {
		GameData data = new PeerConnectData(this.data.username());
		GameInput input = new PeerConnectInput();
		GameLogic logic = new PeerConnectLogic(this.data.response());
		GameVisuals visuals = new PeerConnectVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		return context;
	}

}
