package nomadrealms.app.context;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayDeque;
import java.util.Queue;

import engine.context.GameContext;
import engine.context.input.event.PacketReceivedInputEvent;
import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkingReceiver;
import engine.networking.NetworkingSender;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.event.networking.PingSyncedEvent;
import nomadrealms.event.networking.PongSyncedEvent;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.render.RenderingEnvironment;

public class ServerContext extends GameContext {

	private RenderingEnvironment re;
	private GameState gameState;
	private final Queue<InputEvent> uiEventChannel = new ArrayDeque<>();

	private final NetworkingReceiver networkingReceiver = new NetworkingReceiver();
	private final NetworkingSender networkingSender = new NetworkingSender();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		gameState = new GameState("Server World", uiEventChannel, new OverworldGenerationStrategy(123456789));
		networkingReceiver.init(44999);
		networkingSender.init();
	}

	@Override
	public void update() {
		if (gameState == null) {
			return;
		}
		gameState.update();
		while (!uiEventChannel.isEmpty()) {
			uiEventChannel.poll();
		}
		networkingReceiver.update((event, address) -> input(event, address));
	}

	public void input(SyncedEvent event, PacketAddress address) {
		System.out.println("Received UDP message from " + address + ": " + event);
		if (event instanceof PingSyncedEvent) {
			PingSyncedEvent ping = (PingSyncedEvent) event;
			System.out.println("Ping message: " + ping.message());
			System.out.println("Ping timestamp: " + ping.timestamp());
			networkingSender.send(new PongSyncedEvent("Pong from server", System.currentTimeMillis()), address);
		}
	}

	@Override
	public void cleanUp() {
		networkingReceiver.cleanUp();
		networkingSender.cleanUp();
	}

	@Override
	public void render(float alpha) {
		background(gameState.weather.skyColor(gameState.frameNumber));
		gameState.render(re);
	}

}
