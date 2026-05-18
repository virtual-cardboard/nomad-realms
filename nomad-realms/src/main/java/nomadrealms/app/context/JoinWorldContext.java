package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import engine.context.GameContext;
import engine.context.input.event.InputCallbackRegistry;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.context.input.networking.packet.address.PacketAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nomadrealms.event.networking.HolePunchInitiationEvent;
import nomadrealms.event.networking.PingSyncedEvent;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent;
import nomadrealms.networking.NetworkGraph;
import nomadrealms.networking.flow.FlowContext;
import nomadrealms.networking.flow.NetworkRole;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.join.JoinWorldInterface;
import nomadrealms.user.Player;

public class JoinWorldContext extends GameContext {

	private RenderingEnvironment re;
	private final NetworkGraph networkGraph = new NetworkGraph();
	private List<Player> onlinePlayers = new ArrayList<>();
	private Player localPlayer;

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

		try {
			serverAddress = new PacketAddress(InetAddress.getByName("localhost"), 44999);
			playerName = "Player-" + UUID.randomUUID().toString().substring(0, 4);
			localPlayer = new Player(playerName, null); // Address is unknown yet

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
			networkGraph.update(new FlowContext(networkGraph, onlinePlayers, localPlayer), NetworkRole.CLIENT);
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
