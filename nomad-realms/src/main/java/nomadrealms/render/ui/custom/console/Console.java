package nomadrealms.render.ui.custom.console;

import static engine.common.colour.Colour.rgba;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate.chunkCoordinateOf;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_KP_ENTER;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.cardplayer.FeralMonkey;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class Console implements UI {

	private final List<String> history = new ArrayList<>();
	private String currentInput = "";
	private boolean active = false;
	private final ConstraintBox screen;
	private final GameState gameState;
	private final RenderingEnvironment re;

	public Console(ConstraintBox screen, GameState gameState, RenderingEnvironment re) {
		this.screen = screen;
		this.gameState = gameState;
		this.re = re;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (!active) {
			return;
		}

		float consoleHeight = 300;
		float inputHeight = 40;
		ConstraintBox consoleBox = new ConstraintBox(
				absolute(0),
				screen.h().add(absolute(-consoleHeight)),
				screen.w(),
				absolute(consoleHeight)
		);

		// Render background
		re.defaultShaderProgram
				.set("color", toRangedVector(rgba(0, 0, 0, 150)))
				.set("transform", new Matrix4f(consoleBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

		// Render input box background
		ConstraintBox inputBox = new ConstraintBox(
				absolute(0),
				screen.h().add(absolute(-inputHeight)),
				screen.w(),
				absolute(inputHeight)
		);
		re.defaultShaderProgram
				.set("color", toRangedVector(rgba(30, 30, 30, 200)))
				.set("transform", new Matrix4f(inputBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));

		// Render current input
		re.textRenderer.alignLeft().alignBottom();
		re.textRenderer.render(
				10, screen.h().get() - 5,
				"> " + currentInput,
				screen.w().get() - 20,
				re.font, 30, rgba(255, 255, 255, 255));

		// Render history
		re.textRenderer.alignLeft().alignBottom();
		float y = screen.h().get() - inputHeight - 5;
		for (int i = history.size() - 1; i >= 0; i--) {
			String line = history.get(i);
			re.textRenderer.render(
					10, y,
					line,
					screen.w().get() - 20,
					re.font, 24, rgba(200, 200, 200, 255));
			y -= 30; // Assuming line height
			if (y < screen.h().get() - consoleHeight) {
				break;
			}
		}
	}

	public boolean active() {
		return active;
	}

	public void active(boolean active) {
		this.active = active;
	}

	public void handleKey(int key) {
		if (key == GLFW_KEY_ENTER || key == GLFW_KEY_KP_ENTER) {
			if (!currentInput.isEmpty()) {
				history.add("> " + currentInput);
				String output = processCommand(currentInput);
				if (output != null) {
					history.add(output);
				}
				currentInput = "";
			} else {
				active = false;
			}
		} else if (key == GLFW_KEY_ESCAPE) {
			active = false;
		} else if (key == GLFW_KEY_BACKSPACE) {
			if (!currentInput.isEmpty()) {
				currentInput = currentInput.substring(0, currentInput.length() - 1);
			}
		}
	}

	public void handleChar(int codepoint) {
		currentInput += (char) codepoint;
	}

	private String processCommand(String command) {
		String[] parts = command.trim().split("\\s+");
		String cmd = parts[0];

		if (cmd.equalsIgnoreCase("HELLO")) {
			return "Hello, Nomad!";
		} else if (cmd.equalsIgnoreCase("CLEAR")) {
			history.clear();
			return null;
		} else if (cmd.equalsIgnoreCase("PING")) {
			return "PONG!";
		} else if (cmd.equalsIgnoreCase("HELP")) {
			return "Commands: HELLO, CLEAR, PING, HELP, FIND <type>";
		} else if (cmd.equalsIgnoreCase("FIND")) {
			if (parts.length < 2) {
				return "Usage: FIND <NOMAD|CHIEF|MONKEY>";
			}
			return findEntity(parts[1]);
		}
		return "Unknown command: " + command;
	}

	private String findEntity(String type) {
		Class<? extends Actor> targetClass;
		String typeUpper = type.toUpperCase();
		if (typeUpper.equals("NOMAD")) {
			targetClass = Nomad.class;
		} else if (typeUpper.equals("CHIEF")) {
			targetClass = VillageChief.class;
		} else if (typeUpper.equals("MONKEY")) {
			targetClass = FeralMonkey.class;
		} else {
			return "Unknown entity type: " + type;
		}

		Vector2f center = re.camera.position().vector();
		ChunkCoordinate centerChunkCoord = chunkCoordinateOf(center);
		Queue<ChunkCoordinate> queue = new LinkedList<>();
		queue.add(centerChunkCoord);
		Set<ChunkCoordinate> visited = new HashSet<>();
		visited.add(centerChunkCoord);

		long startTime = System.currentTimeMillis();
		// Search up to 50 layers deep (approx 2500 chunks) to avoid infinite loops if no actor exists
		// But break early if we find an actor in the current layer (because BFS guarantees nearest layer first)

		while (!queue.isEmpty()) {
			ChunkCoordinate currentCoord = queue.poll();

			// We need to check if the chunk actually exists or if getChunk generates it.
			// Ideally we don't want to generate chunks just for searching.
			// Assuming getChunk might be slow if it generates.
			Chunk chunk = gameState.world.getChunk(currentCoord);

			// Check actors in this chunk
			List<Actor> actorsInChunk = chunk.actors();
			if (!actorsInChunk.isEmpty()) {
				Actor nearestInChunk = actorsInChunk.stream()
						.filter(targetClass::isInstance)
						.min(Comparator.comparingDouble(actor ->
								actor.tile().getScreenPosition(re).vector().sub(new Vector2f(re.config.getWidth() / 2f, re.config.getHeight() / 2f)).lengthSquared()
						))
						.orElse(null);

				if (nearestInChunk != null) {
					// We found a chunk with the actor.
					// Since we are doing BFS, this chunk is roughly the closest chunk.
					// However, a neighboring chunk in the next layer might technically have an actor closer
					// to the center if the camera is near the chunk boundary.
					// For "snap to nearest", being "nearest chunk" is usually good enough approximation for "nearest actor"
					// especially if we are talking about finding *any* actor of that type.

					Vector2f currentCameraPos = re.camera.position().vector();
					Vector2f actorScreenPos = nearestInChunk.tile().getScreenPosition(re).vector();
					Vector2f screenCenter = new Vector2f(re.config.getWidth() / 2f, re.config.getHeight() / 2f);
					float zoom = re.camera.zoom().get();

					// Calculate offset from center of screen to actor
					// ScreenPos = (WorldPos - CamPos) * Zoom + ScreenCenterOffset?
					// No, looking at Tile.getScreenPosition:
					// return chunk.pos().add(indexPosition()).sub(re.camera.position()).scale(re.camera.zoom());
					// So ScreenPos = (TileWorldPos - CamPos) * Zoom.
					// We want ScreenPos to be (0,0) (relative to camera center? No, usually camera is top-left in 2D or center?)
					// Actually camera implementation in Tile.render seems to not add half-screen width/height.
					// "re.camera.position()" usually implies the top-left coordinate of the viewport if 0,0 is top-left.

					// If we want to center the camera on the actor:
					// NewCamPos = ActorWorldPos - (ScreenSize / 2 / Zoom)

					// Let's re-read Camera.zoom(float zoom, Mouse mouse).
					// this.position = this.position.add(mouse.coordinate().scale(1 / this.zoom - 1 / zoom));

					// And Tile.getScreenPosition:
					// chunk.pos().add(indexPosition()).sub(re.camera.position()).scale(re.camera.zoom())

					// If we want the actor to be at ScreenCenter (W/2, H/2):
					// (ActorWorldPos - NewCamPos) * Zoom = ScreenCenter
					// ActorWorldPos - NewCamPos = ScreenCenter / Zoom
					// NewCamPos = ActorWorldPos - ScreenCenter / Zoom

					// Let's get ActorWorldPos first.
					// We don't have a direct "get world position" on Actor/Tile easily exposed as a single Vector2f,
					// but we can deduce it from ScreenPosition.
					// ActorScreenPos (current) = (ActorWorldPos - CurrentCamPos) * Zoom
					// ActorWorldPos = ActorScreenPos / Zoom + CurrentCamPos

					Vector2f actorWorldPos = actorScreenPos.scale(1f / zoom).add(currentCameraPos);
					Vector2f newCameraPos = actorWorldPos.sub(screenCenter.scale(1f / zoom));

					re.camera.position(newCameraPos);

					return "Found " + nearestInChunk.name() + "!";
				}
			}

			// BFS expansion
			// Add neighbors to queue
			List<ChunkCoordinate> neighbors = new ArrayList<>();
			neighbors.add(currentCoord.up());
			neighbors.add(currentCoord.down());
			neighbors.add(currentCoord.left());
			neighbors.add(currentCoord.right());

			for (ChunkCoordinate neighbor : neighbors) {
				if (visited.add(neighbor)) {
					// Optional: Check distance to avoid searching entire world
					if (neighbor.sub(centerChunkCoord).toVector2f().length() < 25000) { // Limit search radius
						queue.add(neighbor);
					}
				}
			}

			if (System.currentTimeMillis() - startTime > 500) { // 500ms timeout
				return "Search timed out.";
			}
		}

		return "No " + type + " found nearby.";
	}

}
