package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import engine.common.colour.Colour;
import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.networking.packet.address.PacketAddress;
import engine.visuals.rendering.text.TextFormat;
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
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.networking.Connection;
import nomadrealms.networking.ConnectionState;
import nomadrealms.networking.NetworkGraph;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.join.JoinWorldInterface;
import nomadrealms.user.Player;

public class JoinWorldContext extends GameContext {

	private RenderingEnvironment re;
	private final NetworkGraph networkGraph = new NetworkGraph();
	private ClientSyncedEventHandler eventHandler;
	private List<Player> onlinePlayers = new ArrayList<>();

	private PacketAddress serverAddress;
	private String playerName;

	private JoinWorldInterface joinWorldInterface;
	private final InputCallbackRegistry inputCallbackRegistry = new InputCallbackRegistry();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		networkGraph.init();

		joinWorldInterface = new JoinWorldInterface(re, glContext(), inputCallbackRegistry);
		joinWorldInterface.initHomeButton(() -> {
			if (serverAddress != null && playerName != null) {
				networkGraph.send(new DisconnectFromServerEvent(playerName), serverAddress);
			}
			transition(new HomeScreenContext());
		});
		joinWorldInterface.initConnectToPeersButton(() -> {
			if (serverAddress != null) {
				networkGraph.send(new HolePunchInitiationEvent(), serverAddress);
			}
		});

		eventHandler = new ClientSyncedEventHandler(onlinePlayers, networkGraph);

		try {
			serverAddress = new PacketAddress(InetAddress.getByName("localhost"), 44999);
			playerName = "Player-" + UUID.randomUUID().toString().substring(0, 4);

			System.out.println("Sending PingSyncedEvent to " + serverAddress);
			networkGraph.send(new PingSyncedEvent("Ping from PingApp", System.currentTimeMillis()), serverAddress);
			System.out.println("Sending connect event to " + serverAddress);
			networkGraph.send(new ConnectToServerEvent(playerName), serverAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (initialized()) {
			networkGraph.update(eventHandler::handle);
			for (Connection connection : networkGraph.connections()) {
				if (connection.state() == ConnectionState.LISTENING) {
					networkGraph.send(new HolePunchEvent(connection.nonce()), connection.targetAddress());
				} else if (connection.state() == ConnectionState.RECEIVING) {
					networkGraph.send(new HolePunchSuccessConfirmationEvent(connection.nonce()), connection.targetAddress());
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
		networkGraph.cleanUp();
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
