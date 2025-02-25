package nomadrealms.render.ui;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static visuals.constraint.posdim.MultiplierConstraint.factor;

import java.util.List;
import java.util.function.Consumer;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import nomadrealms.game.GameState;
import nomadrealms.game.world.map.area.Region;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.box.ConstraintBox;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class MapTab implements UI {

	ConstraintBox screen;
	ConstraintBox constraintBox;

	GameState state;

	Vector2f prevMouse = new Vector2f(0, 0);
	boolean isDragging = false;
	Vector2f offset = new Vector2f(0, 0);

	public MapTab(GameState state, ConstraintBox screen,
				  List<Consumer<MousePressedInputEvent>> onClick,
				  List<Consumer<MouseMovedInputEvent>> onDrag,
				  List<Consumer<MouseReleasedInputEvent>> onDrop) {
		this.state = state;
		this.screen = screen;
		constraintBox = new ConstraintBox(
				screen.x().add(screen.w().multiply(0.2f)),
				screen.y().add(screen.h().multiply(0.2f)),
				factor(screen.w(), 0.6f),
				factor(screen.h(), 0.6f)
		);
		addCallbacks(onClick, onDrag, onDrop);
	}

	private void addCallbacks(List<Consumer<MousePressedInputEvent>> onClick,
							  List<Consumer<MouseMovedInputEvent>> onDrag,
							  List<Consumer<MouseReleasedInputEvent>> onDrop) {
		onClick.add(
				(event) -> {
					prevMouse = event.mouse().coordinate().value().toVector();
					isDragging = true;
				}
		);
		onDrag.add(
				(event) -> {
					if (isDragging) {
						Vector2f currMouse = event.mouse().coordinate().value().toVector();
						offset = offset.add(currMouse.sub(prevMouse));
						prevMouse = currMouse;
					}
				}
		);
		onDrop.add(
				(event) -> {
					isDragging = false;
				}
		);
	}

	@Override
	public void render(RenderingEnvironment re) {
		if (!state.showMap) {
			return;
		}
		//        re.fbo1.render(
		//                () -> {
		//                    int colour = rgba(0, 0, 0, 0);
		//                    glClearColor(normalizedR(colour), normalizedG(colour), normalizedB(colour), normalizedA(colour));
		//                    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		//                    for (Chunk chunk : state.world.chunks()) {
		//                        Vector2f screenPos = offset.add(50 * chunk.x(), 50 * chunk.y());
		//                        re.defaultShaderProgram
		//                                .set("color", toRangedVector(rgb(100, 0, 0)))
		//                                .set("transform", new Matrix4f(screenPos.x(), screenPos.y(), 50, 50, re.glContext))
		//                                .use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		//                    }
		//                }
		//        );
		DefaultFrameBuffer.instance().render(
				() -> {
					for (Region region : state.world.map().regions()) {
						Vector2f screenPos = re.glContext.screen.size().value().toVector().scale(0.5f)
								.add(offset)
								.add(50 * region.coord().x(), 50 * region.coord().y());
						re.defaultShaderProgram
								.set("color", toRangedVector(rgb(100, 0, 0)))
								.set("transform", new Matrix4f(screenPos.x(), screenPos.y(), 50, 50, re.glContext))
								.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
					}
				}
		);
		//        DefaultFrameBuffer.instance().render(
		//                () -> {
		//                    re.textureRenderer.render(re.fbo1.texture(), 0, 0, 100, 100);
		//                }
		//        );
	}

}
