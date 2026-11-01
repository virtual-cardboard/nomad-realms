package nomadrealms.render.ui.custom.game;

import static engine.common.colour.Colour.rgba;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.context.input.Mouse;
import engine.context.input.event.InputCallbackRegistry;
import engine.nengen.DrawBatch;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintPair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.PlayerMoveInputEvent;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.ui.custom.card.Arrow;
import nomadrealms.render.ui.custom.card.DeckTab;
import nomadrealms.user.Player;

public class PlayerMovementUI implements UI {

	private final Player localPlayer;
	private final GameState state;
	private final Mouse mouse;
	private final Consumer<InputEvent> actionEventChannel;
	private final DeckTab deckTab;

	private RenderingEnvironment re;
	private boolean isDraggingPlayer = false;
	private final DrawBatch highlightBatch = new DrawBatch();

	public PlayerMovementUI(Player localPlayer, GameState state, Mouse mouse, InputCallbackRegistry registry,
							Consumer<InputEvent> actionEventChannel, DeckTab deckTab) {
		this.localPlayer = localPlayer;
		this.state = state;
		this.mouse = mouse;
		this.actionEventChannel = actionEventChannel;
		this.deckTab = deckTab;

		addCallbacks(registry);
	}

	private void addCallbacks(InputCallbackRegistry registry) {
		registry.registerOnPress(event -> {
			if (event.button() == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				if (deckTab != null && deckTab.selectedCard() != null) {
					return;
				}
				CardPlayer nomad = localPlayer.cardPlayer(state.world());
				if (nomad == null || nomad.tile() == null) {
					return;
				}
				TileCoordinate ghostCoord = nomad.getGhostTileCoordinate();
				if (ghostCoord == null) {
					return;
				}
				Tile ghostTile = state.world().getTile(ghostCoord);
				if (re == null || re.is == null || re.is.camera == null) {
					return;
				}
				Tile mouseTile = state.getMouseHexagon(mouse, re.is.camera);
				if (mouseTile != null && ghostTile != null && mouseTile.coord().equals(ghostTile.coord())) {
					isDraggingPlayer = true;
				}
			} else if (event.button() == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
				if (isDraggingPlayer) {
					isDraggingPlayer = false;
				}
			}
		});

		registry.registerOnDrop(event -> {
			if (event.button() == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT && isDraggingPlayer) {
				isDraggingPlayer = false;
				CardPlayer nomad = localPlayer.cardPlayer(state.world());
				if (nomad == null || nomad.tile() == null) {
					return;
				}
				TileCoordinate ghostCoord = nomad.getGhostTileCoordinate();
				if (ghostCoord == null) {
					return;
				}
				Tile ghostTile = state.world().getTile(ghostCoord);
				if (ghostTile == null || re == null || re.is == null || re.is.camera == null) {
					return;
				}
				Tile mouseTile = state.getMouseHexagon(mouse, re.is.camera);
				List<Tile> neighbors = getAdjacentTiles(state.world(), ghostTile);
				if (mouseTile != null && neighbors.contains(mouseTile)) {
					boolean valid = mouseTile.walkable() && (mouseTile.actor() == null || mouseTile.actor() == nomad);
					if (valid) {
						actionEventChannel.accept(new PlayerMoveInputEvent(nomad, mouseTile.coord()));
					}
				}
			}
		});
	}

	@Override
	public void render(RenderingEnvironment re) {
		this.re = re;
		CardPlayer nomad = localPlayer.cardPlayer(state.world());
		if (nomad == null || nomad.tile() == null) {
			return;
		}

		TileCoordinate ghostCoord = nomad.getGhostTileCoordinate();
		if (ghostCoord == null) {
			return;
		}
		Tile ghostTile = state.world().getTile(ghostCoord);
		if (ghostTile == null) {
			return;
		}

		if (isDraggingPlayer) {
			List<Tile> neighbors = getAdjacentTiles(state.world(), ghostTile);
			float scale = re.is.camera.zoom().get();
			float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
			float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;
			re.hexagonRenderer.prepareInstanced(width, height);
			highlightBatch.vao(RectangleVertexArrayObject.instance())
					.shaderProgram(re.hexagonRenderer.instancedProgram())
					.glContext(re.glContext)
					.clear();

			for (Tile n : neighbors) {
				boolean valid = n.walkable() && (n.actor() == null || n.actor() == nomad);
				int color = valid ? rgba(100, 100, 255, 100) : rgba(255, 100, 100, 100);
				ConstraintPair pos = n.getScreenPosition(re);
				Matrix4f transform = new Matrix4f(
						pos.x().get() - width * 0.5f, pos.y().get() - height * 0.5f,
						width, height, re.glContext);
				highlightBatch.add(re.hexagonRenderer.padTransform(transform), color);
			}
			highlightBatch.draw();

			Tile mouseTile = state.getMouseHexagon(mouse, re.is.camera);
			ConstraintPair targetPos;
			if (mouseTile != null && neighbors.contains(mouseTile) && mouseTile.walkable() && (mouseTile.actor() == null || mouseTile.actor() == nomad)) {
				targetPos = mouseTile.getScreenPosition(re);
			} else {
				targetPos = mouse.coordinate();
			}

			ConstraintPair ghostOriginPos = ghostTile.getScreenPosition(re);
			new Arrow(ghostOriginPos, targetPos).render(re);
			renderGhostSprite(re, nomad, targetPos);

			if (!ghostTile.coord().equals(nomad.tile().coord())) {
				new Arrow(nomad.getScreenPosition(re), ghostOriginPos).render(re);
				renderGhostSprite(re, nomad, ghostOriginPos);
			}
		} else {
			if (!ghostTile.coord().equals(nomad.tile().coord())) {
				ConstraintPair nomadPos = nomad.getScreenPosition(re);
				ConstraintPair ghostPos = ghostTile.getScreenPosition(re);
				new Arrow(nomadPos, ghostPos).render(re);
				renderGhostSprite(re, nomad, ghostPos);
			}
		}
	}

	private void renderGhostSprite(RenderingEnvironment re, CardPlayer player, ConstraintPair position) {
		float scale = player.imageScale() * TILE_RADIUS * re.is.camera.zoom().get();
		Vector2f pos = position.vector();
		re.textureRenderer.setDiffuse(rgba(100, 150, 255, 180));
		re.textureRenderer.render(
				re.imageMap.get(player.imageName()),
				pos.x() - 0.5f * scale,
				pos.y() - 0.7f * scale,
				scale, scale);
		re.textureRenderer.resetDiffuse();
	}

	public List<Tile> getAdjacentTiles(World world, Tile tile) {
		List<Tile> list = new ArrayList<>(6);
		if (tile == null) return list;
		Tile ul = tile.ul(world); if (ul != null) list.add(ul);
		Tile um = tile.um(world); if (um != null) list.add(um);
		Tile ur = tile.ur(world); if (ur != null) list.add(ur);
		Tile dl = tile.dl(world); if (dl != null) list.add(dl);
		Tile dm = tile.dm(world); if (dm != null) list.add(dm);
		Tile dr = tile.dr(world); if (dr != null) list.add(dr);
		return list;
	}

	public boolean isDraggingPlayer() {
		return isDraggingPlayer;
	}

}
