package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.common.math.Matrix4f.screenToPixel;
import static nomadrealms.context.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.context.input.Mouse;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.lwjgl.GLContext;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.condition.Condition;
import nomadrealms.context.game.card.target.TargetType;
import nomadrealms.context.game.card.target.TargetingInfo;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.vao.shape.HexagonVao;

public class TargetingArrow implements UI {

	UICard origin;
	Mouse mouse;
	TargetingInfo info;
	Target target;

	GameState state;

	public TargetingArrow(GameState state) {
		this.state = state;
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (origin == null || mouse == null) {
			return;
		}
		Tile tile = state.getMouseHexagon(mouse, re.camera);
		Vector2f screenPosition = tile.getScreenPosition(re).vector();

		if (info.targetType() == TargetType.HEXAGON) {
			target = tile;
			if (target == null || !checkConditions(info, state.world(), target, state.world().nomad)) {
				return;
			}
			re.defaultShaderProgram
					.set("color", toRangedVector(rgb(255, 255, 0)))
					.set("transform", new Matrix4f(
							screenPosition.x(), screenPosition.y(),
							TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f,
							TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f,
							re.glContext))
					.use(new DrawFunction()
							.vao(HexagonVao.instance())
							.glContext(re.glContext)
					);
		}
		if (info.targetType() == TargetType.CARD_PLAYER) {
			target = tile.actor();
			if (target == null || !checkConditions(info, state.world(), target, state.world().nomad)) {
				return;
			}
			re.defaultShaderProgram
					.set("color", toRangedVector(rgb(255, 255, 0)))
					.set("transform", new Matrix4f(
							screenPosition.x(), screenPosition.y(),
							TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f,
							TILE_RADIUS * 2 * SIDE_LENGTH * 0.98f,
							re.glContext))
					.use(new DrawFunction()
							.vao(HexagonVao.instance())
							.glContext(re.glContext)
					);
		}
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(0, 0, 0)))
				.set("transform", lineTransform(re.glContext, mouse.coordinate().vector(),
						origin.centerPosition().vector()))
				.use(
						new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext)
				);
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

	private boolean checkConditions(TargetingInfo info, World world, Target target, CardPlayer source) {
		for (Condition condition : info.conditions()) {
			if (!condition.test(world, target, source)) {
				return false;
			}
		}
		return true;
	}

	private Matrix4f lineTransform(GLContext glContext, Vector2f point1, Vector2f point2) {
		float angle = (float) Math.atan2(point2.y() - point1.y(), point2.x() - point1.x());
		return screenToPixel(glContext)
				.translate(point1.x(), point1.y())
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate(angle, new Vector3f(0, 0, 1))
				.translate(0, -5, 0)
				.scale(point1.sub(point2).length(), 3);
	}

	public Target target() {
		return target;
	}

}
