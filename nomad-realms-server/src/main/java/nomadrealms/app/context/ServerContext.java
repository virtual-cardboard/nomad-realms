package nomadrealms.app.context;

import engine.context.GameContext;
import engine.networking.NetworkNode;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InputEventFrame;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.user.Player;
import engine.visuals.rendering.text.TextFormat;
import engine.common.colour.Colour;
import nomadrealms.render.ui.custom.console.Console;
import nomadrealms.render.ui.Camera;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.CharacterTypedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;
import java.io.PrintStream;
import java.io.OutputStream;
import java.io.IOException;

public class ServerContext extends GameContext {

	private RenderingEnvironment re;
	private GameState gameState;
	private Console console;
	private final Queue<InputEvent> uiEventChannel = new ArrayDeque<>();

	private final NetworkNode networkNode = new NetworkNode();
	private ServerSyncedEventHandler eventHandler;

	private final List<Player> onlinePlayers = new CopyOnWriteArrayList<>();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		re.camera = new Camera(0, 0);
		gameState = new GameState("Server World", uiEventChannel, new OverworldGenerationStrategy(123456789));
		console = new Console(glContext().screen, gameState, re);
		networkNode.init(44999);
		eventHandler = new ServerSyncedEventHandler(networkNode, onlinePlayers);
		redirectSystemStreams();
	}

	private void redirectSystemStreams() {
		PrintStream out = System.out;
		PrintStream err = System.err;

		System.setOut(new PrintStream(new ConsoleOutputStream(out)));
		System.setErr(new PrintStream(new ConsoleOutputStream(err)));
	}

	private class ConsoleOutputStream extends OutputStream {
		private final PrintStream original;
		private final StringBuilder buffer = new StringBuilder();

		public ConsoleOutputStream(PrintStream original) {
			this.original = original;
		}

		@Override
		public synchronized void write(int b) throws IOException {
			original.write(b);
			if (b == '\n') {
				console.println(buffer.toString());
				buffer.setLength(0);
			} else if (b != '\r') {
				buffer.append((char) b);
			}
		}
	}

	@Override
	public void update() {
		if (gameState == null) {
			return;
		}
		gameState.update(new InputEventFrame(gameState.frameNumber));
		while (!uiEventChannel.isEmpty()) {
			uiEventChannel.poll();
		}
		networkNode.update(eventHandler::handle);
	}

	@Override
	public void cleanUp() {
		networkNode.cleanUp();
	}

	@Override
	public void input(KeyPressedInputEvent event) {
		int key = event.code();
		if (console.active()) {
			console.handleKey(key);
			return;
		}
		if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
			console.active(true);
		}
	}

	@Override
	public void input(CharacterTypedInputEvent event) {
		if (console.active()) {
			console.handleChar(event.codepoint());
		}
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
		if (console.active()) {
			console.handleScroll(event.yAmount());
		}
	}

	@Override
	public void render(float alpha) {
		background(gameState.weather.skyColor(gameState.frameNumber));
		gameState.render(re);
		console.render(re);

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

}
