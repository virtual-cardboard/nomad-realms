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
import engine.visuals.rendering.text.TextFormat;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import nomadrealms.event.networking.PingSyncedEvent;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.custom.join.JoinWorldInterface;
import nomadrealms.user.Player;

public class JoinWorldContext extends GameContext {

	private RenderingEnvironment re;
	private final NetworkNode networkNode = new NetworkNode();
	private ClientSyncedEventHandler eventHandler;
	private List<Player> onlinePlayers = new ArrayList<>();

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

		eventHandler = new ClientSyncedEventHandler(onlinePlayers);

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
			networkNode.update(eventHandler::handle);
		}
	}

	@Override
	public void render(float alpha) {
		background(rgb(50, 50, 50));
		joinWorldInterface.render(re, onlinePlayers);
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
