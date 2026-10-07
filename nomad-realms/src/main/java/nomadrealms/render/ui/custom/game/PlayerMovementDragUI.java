package nomadrealms.render.ui.custom.game;

import static engine.common.colour.Colour.rgba;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.context.input.Mouse;
import engine.context.input.event.InputCallbackRegistry;
import engine.nengen.DrawBatch;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.Texture;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.PlayerMoveInputEvent;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.ui.custom.card.Arrow;

public class PlayerMovementDragUI implements UI {

	private final RenderingEnvironment re;
	private final GameState state;
	private final CardPlayer player;
	private final Mouse mouse;
	private final Consumer<InputEvent> actionEventChannel;

	private boolean isDragging = false;
	private Tile dragStartTile = null;

	private final DrawBatch tileBatch = new DrawBatch();

	public PlayerMovementDragUI(RenderingEnvironment re, GameState state, CardPlayer player, Mouse mouse,
								InputCallbackRegistry registry, Consumer<InputEvent> actionEventChannel) {
		this.re = re;
		this.state = state;
		this.player = player;
		this.mouse = mouse;
		this.actionEventChannel = actionEventChannel;
		addCallbacks(registry);
	}

	private void addCallbacks(InputCallbackRegistry registry) {
		registry.registerOnPress(event -> {
			if (event.button() == GLFW_MOUSE_BUTTON_LEFT && player != null && !player.dead()) {
				Tile ghostTile = player.movementQueueEndTile();
				if (ghostTile != null) {
					Tile mouseTile = state.getMouseHexagon(mouse, re.is.camera);
					boolean clickedGhost = false;
					if (mouseTile != null && mouseTile.equals(ghostTile)) {
						clickedGhost = true;
					}
					if (clickedGhost) {
						isDragging = true;
						dragStartTile = ghostTile;
					}
				}
			}
		});

		registry.registerOnDrop(event -> {
			if (event.button() == GLFW_MOUSE_BUTTON_LEFT && isDragging) {
				isDragging = false;
				if (dragStartTile != null) {
					Tile hoveredTile = state.getMouseHexagon(mouse, re.is.camera);
					if (hoveredTile != null && dragStartTile.coord().distanceTo(hoveredTile.coord()) == 1) {
						if (player.isTileValidForMovement(hoveredTile)) {
							actionEventChannel.accept(new PlayerMoveInputEvent(player, hoveredTile));
						}
					}
				}
				dragStartTile = null;
			}
		});
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (player == null || player.dead()) {
			return;
		}

		Tile ghostTile = player.movementQueueEndTile();
		Tile currentTile = player.tile();
		if (ghostTile == null) {
			ghostTile = currentTile;
		}

		float scale = re.is.camera.zoom().get();
		float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
		float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;

		if (isDragging && dragStartTile != null) {
			// Highlight radius 1 tiles around dragStartTile
			List<Tile> neighbors = getAdjacentTiles(dragStartTile);

			re.hexagonRenderer.prepareInstanced(width, height);
			tileBatch.vao(RectangleVertexArrayObject.instance())
					.shaderProgram(re.hexagonRenderer.instancedProgram())
					.glContext(re.glContext)
					.clear();

			Tile hoveredTile = state.getMouseHexagon(mouse, re.is.camera);

			for (Tile t : neighbors) {
				if (t == null) continue;
				ConstraintPair pos = t.getScreenPosition(re);
				Matrix4f transform = new Matrix4f(
						pos.x().get() - width * 0.5f, pos.y().get() - height * 0.5f,
						width,
						height,
						re.glContext);

				boolean valid = player.isTileValidForMovement(t);
				int color;
				if (t.equals(hoveredTile)) {
					color = valid ? rgba(100, 100, 255, 180) : rgba(255, 80, 80, 180);
				} else {
					color = valid ? rgba(100, 100, 255, 100) : rgba(255, 80, 80, 100);
				}
				tileBatch.add(re.hexagonRenderer.padTransform(transform), color);
			}
			tileBatch.draw();

			// Render Arrow from dragStartTile (or player pos) to mouse or target
			ConstraintPair arrowStart = player.getScreenPosition(re);
			ConstraintPair arrowEnd;
			if (hoveredTile != null && dragStartTile.coord().distanceTo(hoveredTile.coord()) == 1) {
				arrowEnd = hoveredTile.getScreenPosition(re);
			} else {
				arrowEnd = mouse.coordinate();
			}
			new Arrow(arrowStart, arrowEnd).render(re);

			// Render blue-shifted ghost at arrowEnd
			renderGhostSprite(re, arrowEnd);
		} else {
			// Not dragging: render ghost if ghost is not at player's current visual position
			ConstraintPair ghostPos = ghostTile.getScreenPosition(re);
			ConstraintPair playerPos = player.getScreenPosition(re);

			boolean showGhost = !ghostTile.equals(currentTile) || !player.movementQueue().isEmpty();
			if (showGhost) {
				new Arrow(playerPos, ghostPos).render(re);
				renderGhostSprite(re, ghostPos);
			}
		}
	}

	private List<Tile> getAdjacentTiles(Tile center) {
		List<Tile> neighbors = new ArrayList<>();
		if (center == null) return neighbors;
		neighbors.add(center.ul(state.world()));
		neighbors.add(center.um(state.world()));
		neighbors.add(center.ur(state.world()));
		neighbors.add(center.dl(state.world()));
		neighbors.add(center.dm(state.world()));
		neighbors.add(center.dr(state.world()));
		return neighbors;
	}

	private void renderGhostSprite(RenderingEnvironment re, ConstraintPair screenPos) {
		float zoom = re.is.camera.zoom().get();
		float imgScale = player.imageScale();
		float scale = 0.6f * TILE_RADIUS * zoom * imgScale;
		Vector2f pos = screenPos.vector();

		Texture image = re.imageMap.get(player.imageName());
		if (image == null) {
			return;
		}
		re.textureRenderer.setDiffuse(rgba(100, 160, 255, 200));
		re.textureRenderer.render(
				image,
				pos.x() - 0.5f * scale,
				pos.y() - 0.7f * scale,
				scale, scale);
		re.textureRenderer.resetDiffuse();
	}

	public boolean isDragging() {
		return isDragging;
	}

}
