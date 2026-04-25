package nomadrealms.app.context;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayDeque;
import java.util.Queue;

import engine.context.GameContext;
import engine.context.input.event.PacketReceivedInputEvent;
import engine.networking.NetworkingReceiver;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.render.RenderingEnvironment;

public class ServerContext extends GameContext {

	private RenderingEnvironment re;
	private GameState gameState;
	private final Queue<InputEvent> uiEventChannel = new ArrayDeque<>();
	private final java.util.List<InputEventFrame> inputFrames = new java.util.ArrayList<>();

	private final NetworkingReceiver networkingReceiver = new NetworkingReceiver();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		gameState = new GameState("Server World", uiEventChannel, new OverworldGenerationStrategy(123456789));
		networkingReceiver.init(44999);
	}

	@Override
	public void update() {
		if (gameState == null) {
			return;
		}
		inputFrames.add(new InputEventFrame(gameState.frameNumber + 1));
		if (inputFrames.size() > 30) {
			inputFrames.remove(0);
		}
		gameState.update(inputFrames.get(inputFrames.size() - 1));
		while (!uiEventChannel.isEmpty()) {
			uiEventChannel.poll();
		}
		networkingReceiver.update(this::input);
	}

	public void input(SyncedEvent event) {
		System.out.println("Received UDP message: " + event);
	}

	@Override
	public void cleanUp() {
		networkingReceiver.cleanUp();
	}

	@Override
	public void render(float alpha) {
		background(gameState.weather.skyColor(gameState.frameNumber));
		gameState.render(re);
	}

}
