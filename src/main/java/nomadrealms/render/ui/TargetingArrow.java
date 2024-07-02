package nomadrealms.render.ui;

import common.math.Matrix4f;
import common.math.Vector2f;
import common.math.Vector3f;
import context.input.Mouse;
import nomadrealms.game.GameState;
import nomadrealms.game.card.UICard;
import nomadrealms.game.card.target.TargetType;
import nomadrealms.game.card.target.TargetingInfo;
import nomadrealms.game.event.Target;
import nomadrealms.game.world.map.area.Tile;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.vao.shape.HexagonVao;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.lwjgl.GLContext;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static common.math.Matrix4f.screenToPixel;
import static nomadrealms.game.world.map.area.Tile.TILE_RADIUS;
import static nomadrealms.render.vao.shape.HexagonVao.SIDE_LENGTH;

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
		Tile tile = state.getMouseHexagon(mouse);
		DefaultFrameBuffer.instance().render(() -> {
					if (info.targetType() == TargetType.HEXAGON) {
						target = tile;
//						if (target == null) {
//							return;
//						}
						Vector2f screenPosition = tile.getScreenPosition(re);
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
						target = state.world.getTargetOnTile(tile);
						if (target == null) {
							return;
						}
						Vector2f screenPosition = tile.getScreenPosition(re);
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
							.set("transform", lineTransform(re.glContext, mouse.coordinate().value().toVector(),
									origin.centerPosition()))
							.use(
									new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext)
							);
				}
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

	private Matrix4f lineTransform(GLContext glContext, Vector2f point1, Vector2f point2) {
		float angle = (float) Math.atan2(point2.y() - point1.y(), point2.x() - point1.x());
		return screenToPixel(glContext)
				.translate(point1.x(), point1.y())
				.scale(new Vector3f(1, 1, 0f)) // Flatten the z-axis to avoid clipping
				.rotate(angle, new Vector3f(0, 0, 1))
				.translate(0, -5, 0)
				.scale(point1.sub(point2).length(), 3);
	}

}
