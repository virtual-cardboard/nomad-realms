package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import engine.common.colour.Colour;
import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nomadrealms.event.networking.HolePunchEvent;
import nomadrealms.event.networking.HolePunchInitiationEvent;
import nomadrealms.event.networking.HolePunchSuccessConfirmationEvent;
import nomadrealms.event.networking.HolePunchSuccessAcknowledgementEvent;
import nomadrealms.event.networking.PingSyncedEvent;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.networking.Connection;
import nomadrealms.networking.ConnectionState;
import nomadrealms.networking.NetworkGraph;
import nomadrealms.networking.flow.NetworkRole;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.join.JoinWorldInterface;
import nomadrealms.user.Player;

public class JoinWorldContext extends GameContext {

	private RenderingEnvironment re;
	private final NetworkNode networkNode = new NetworkNode();
	private final NetworkGraph networkGraph = new NetworkGraph(NetworkRole.CLIENT);
	private final List<Player> onlinePlayers = new ArrayList<>();

	private PacketAddress serverAddress;
	private String playerName;

	private JoinWorldInterface joinWorldInterface;
	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		networkNode.init();

		joinWorldInterface = new JoinWorldInterface(re, glContext(), inputCallbackRegistry);
		joinWorldInterface.initHomeButton(() -> {
			if (serverAddress != null && playerName != null) {
				networkNode.send(new DisconnectFromServerEvent(playerName), serverAddress);
			}
			transition(new HomeScreenContext());
		});
		joinWorldInterface.initConnectToPeersButton(() -> {
			if (serverAddress != null) {
				networkNode.send(new HolePunchInitiationEvent(), serverAddress);
			}
		});

		try {
			serverAddress = new PacketAddress(InetAddress.getByName("localhost"), 44999);
			playerName = "Player-" + UUID.randomUUID().toString().substring(0, 4);

			System.out.println("Sending PingSyncedEvent to " + serverAddress);
			networkNode.send(new PingSyncedEvent("Ping from PingApp", System.currentTimeMillis()), serverAddress);
			System.out.println("Sending connect event to " + serverAddress);
			networkNode.send(new ConnectToServerEvent(playerName), serverAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (initialized()) {
			networkGraph.update(networkNode, onlinePlayers);
			for (Connection connection : networkGraph.connections()) {
				if (connection.state() == ConnectionState.LISTENING) {
					networkNode.send(new HolePunchEvent(connection.nonce()), connection.targetAddress());
				} else if (connection.state() == ConnectionState.RECEIVING) {
					networkNode.send(new HolePunchSuccessConfirmationEvent(connection.nonce()), connection.targetAddress());
				}
			}
		}
	}

	@Override
	public void render(float alpha) {
		background(rgb(50, 50, 50));
		joinWorldInterface.render(re, onlinePlayers, networkGraph.connections());
	}

	@Override
	public void cleanUp() {
		networkNode.cleanUp();
	}

	@Override
	public void input(MouseMovedInputEvent event) {
		inputCallbackRegistry.triggerOnDrag(event);
	}

	@Override
	public void input(MousePressedInputEvent event) {
		inputCallbackRegistry.triggerOnPress(event);
	}

	@Override
	public void input(MouseReleasedInputEvent event) {
		inputCallbackRegistry.triggerOnDrop(event);
	}

}
