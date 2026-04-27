package nomadrealms.app.context;

import static engine.common.colour.Colour.rgb;

import engine.common.colour.Colour;
import engine.context.GameContext;
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
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.user.Player;

public class JoinWorldContext extends GameContext {

	private RenderingEnvironment re;
	private final NetworkNode networkNode = new NetworkNode();
	private ClientSyncedEventHandler eventHandler;
	private List<Player> onlinePlayers = new ArrayList<>();

	private PacketAddress serverAddress;

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		networkNode.init();

		eventHandler = new ClientSyncedEventHandler(players -> {
			this.onlinePlayers = players;
		});

		try {
			serverAddress = new PacketAddress(InetAddress.getByName("localhost"), 44999);
			String playerName = "Player-" + UUID.randomUUID().toString().substring(0, 4);

			System.out.println("Sending PingSyncedEvent to " + serverAddress);
			networkNode.send(new PingSyncedEvent("Ping from PingApp", System.currentTimeMillis()), serverAddress);
			System.out.println("Sending connect event to " + serverAddress);
			networkNode.send(new ConnectToServerEvent(playerName), serverAddress);
			System.out.println("Fetching online players...");
			networkNode.send(new GetOnlinePlayersEvent(), serverAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update() {
		if (initialized()) {
			System.out.println("Listening");
			networkNode.update(eventHandler::handle);
		}
	}

	@Override
	public void render(float alpha) {
		background(rgb(50, 50, 50));

		// Render UI to show online players
		float startX = 20;
		float startY = 20;
		float width = 300;
		float height = 50 + (onlinePlayers.size() * 30);

		// Draw background box
		re.rectangleRenderer.render(startX, startY, width, height, 10, Colour.rgba(0, 0, 0, 180));

		// Draw Title
		re.textRenderer.render(startX + 10, startY + 10,
				TextFormat.textFormat()
						.text("Online Players: " + onlinePlayers.size())
						.font(re.font)
						.fontSize(20)
						.colour(Colour.rgb(255, 255, 255)));

		// Draw Player List
		float yOffset = startY + 40;
		for (Player p : onlinePlayers) {
			re.textRenderer.render(startX + 10, yOffset,
					TextFormat.textFormat()
							.text(p.name() + " (" + p.address() + ")")
							.font(re.font)
							.fontSize(16)
							.colour(Colour.rgb(200, 200, 200)));
			yOffset += 30;
		}
	}

	@Override
	public void cleanUp() {
		networkNode.cleanUp();
	}

}
