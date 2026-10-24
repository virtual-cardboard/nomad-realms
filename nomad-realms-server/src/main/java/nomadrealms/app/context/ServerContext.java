package nomadrealms.app.context;

import engine.context.GameContext;
import engine.networking.NetworkNode;
import java.util.List;
import nomadrealms.context.game.GameState;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.server.Server;
import nomadrealms.user.Player;
import engine.visuals.rendering.text.TextFormat;
import engine.common.colour.Colour;
import nomadrealms.render.ui.custom.console.Console;
import engine.context.input.event.KeyPressedInputEvent;
import engine.context.input.event.CharacterTypedInputEvent;
import engine.context.input.event.MouseScrolledInputEvent;

public class ServerContext extends GameContext {

	private RenderingEnvironment re;
	private Console console;
	private final Server server = new Server();

	@Override
	public void init() {
		re = new RenderingEnvironment(glContext(), config(), mouse());
		server.initGameState();
		console = new Console(glContext().screen, server.gameState(), re);
		console.active(true);
		console.toggleable(false);
		console.consoleHeight(600);
		server.initNetwork(console);
	}

	@Override
	public void update() {
		server.update();
	}

	@Override
	public void cleanUp() {
		server.cleanUp();
	}

	@Override
	public void input(KeyPressedInputEvent event) {
		console.handleKey(event.code());
	}

	@Override
	public void input(CharacterTypedInputEvent event) {
		console.handleChar(event.codepoint());
	}

	@Override
	public void input(MouseScrolledInputEvent event) {
		console.handleScroll(event.yAmount());
	}

	@Override
	public void render(float alpha) {
		GameState gameState = server.gameState();
		if (gameState == null) {
			return;
		}
		background(gameState.weather.skyColor(gameState.frameNumber));
		gameState.render(re);

		// Render UI to show online players
		List<Player> onlinePlayers = server.onlinePlayers();
		float startX = 20;
		float startY = 20;
		float width = 300;
		float height = 50 + (onlinePlayers.size() * 30);

		// Draw background box
		re.rectangleRenderer.render(startX, startY, width, height, 10, Colour.rgba(0, 0, 0, 180));

		// Draw Title
		re.textRenderer.render(
			TextFormat.textFormat()
				.text("Online Players: " + onlinePlayers.size())
				.font(re.font)
				.fontSize(20)
				.colour(Colour.rgb(255, 255, 255))
				.transform(re.textRenderer.screenToPixel().copy().translate(startX + 10, startY + 10)));

		// Draw Player List
		float yOffset = startY + 40;
		for (Player p : onlinePlayers) {
			re.textRenderer.render(
				TextFormat.textFormat()
					.text(p.name() + " (" + p.address() + ")")
					.font(re.font)
					.fontSize(16)
					.colour(Colour.rgb(200, 200, 200))
					.transform(re.textRenderer.screenToPixel().copy().translate(startX + 10, yOffset)));
			yOffset += 30;
		}
		console.render(re);
	}

}
