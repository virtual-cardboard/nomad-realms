package nomadrealms.render.ui.custom.card;

import engine.common.math.Matrix4f;
import engine.context.input.Mouse;
import engine.nengen.DrawBatch;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.target.TargetType;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Chunk;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

import static engine.common.colour.Colour.rgba;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.render.vao.shape.HexagonVao.HEIGHT;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

public class TargetingArrow implements UI {

	private UICard origin;
	private Mouse mouse;
	private TargetingInfo info;
	private Target target;

	GameState state;
	private final CardPlayer source;

	private final DrawBatch tileBatch = new DrawBatch();

	public TargetingArrow(GameState state, CardPlayer source) {
		this.state = state;
		this.source = source;
	}

	@Override
	public void render(RenderingEnvironment re) {
		target = null;
		if (origin == null || mouse == null) {
			return;
		}

		if (info.targetType() == TargetType.HEXAGON) {
			float scale = re.is.camera.zoom().get();
			float height = TILE_RADIUS * 2 * HEIGHT * 0.98f * scale;
			float width = TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f * scale;
			re.hexagonRenderer.prepareInstanced(width, height);
			tileBatch.vao(RectangleVertexArrayObject.instance())
					.shaderProgram(re.hexagonRenderer.instancedProgram())
					.glContext(re.glContext)
					.clear();
			for (Chunk chunk : state.world().getVisibleChunks(re)) {
				for (Tile t : chunk.tiles()) {
					if (checkConditions(info, state.world(), t, source)) {
						ConstraintPair pos = t.getScreenPosition(re);
						Matrix4f transform = new Matrix4f(
								pos.x().get() - width * 0.5f, pos.y().get() - height * 0.5f,
								width,
								height,
								re.glContext);
						tileBatch.add(re.hexagonRenderer.padTransform(transform), rgba(100, 100, 255, 100));
					}
				}
			}
			tileBatch.draw();
		}

		Tile tile = state.getMouseHexagon(mouse, re.is.camera);
		ConstraintPair screenPosition = null;

		if (info.targetType() == TargetType.HEXAGON) {
			if (tile == null || !checkConditions(info, state.world(), tile, source)) {
				new Arrow(origin.centerPosition(), mouse.coordinate())
						.render(re);
				return;
			}
			target = tile;
			screenPosition = tile.getScreenPosition(re);
		} else if (info.targetType() == TargetType.CARD_PLAYER) {
			if (tile.actor() == null || !checkConditions(info, state.world(), tile.actor(), source)) {
				return;
			}
			target = tile.actor();
			screenPosition = tile.getScreenPosition(re);
		}

		new Arrow(origin.centerPosition(), mouse.coordinate())
				.targetCenter(screenPosition)
				.render(re);
	}

	private boolean checkConditions(TargetingInfo info, World world, Target target, CardPlayer source) {
		return info.conditions().stream().allMatch(c -> c.test(world, target, source));
	}

	public TargetingArrow origin(UICard origin) {
		this.origin = origin;
		return this;
	}

	public TargetingArrow mouse(Mouse mouse) {
		this.mouse = mouse;
		return this;
	}

	/**
	 * May be race condition, keep an eye on this.
	 *
	 * @param targetingInfo
	 * @return
	 */
	public TargetingArrow info(TargetingInfo targetingInfo) {
		this.info = targetingInfo;
		return this;
	}

	public Target target() {
		return target;
	}

	public void target(Target target) {
		this.target = target;
	}

}
