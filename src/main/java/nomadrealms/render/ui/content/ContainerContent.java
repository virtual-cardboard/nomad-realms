package nomadrealms.render.ui.content;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;

import common.math.Matrix4f;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.ConstraintBox;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class ContainerContent extends BasicUIContent {

	private boolean fill = false;
	private int colour;

	public ContainerContent(UIContent parent) {
		super(parent);
	}

	public ContainerContent(UIContent parent, ConstraintBox box) {
		super(parent, box);
	}

	/**
	 * Container content does not render anything.
	 *
	 * @param re the rendering environment
	 */
	@Override
	public void _render(RenderingEnvironment re) {
		if (fill) {
			DefaultFrameBuffer.instance().render(() -> {
				re.defaultShaderProgram
						.set("color", toRangedVector(rgb(100, 0, 0)))
						.set("transform", new Matrix4f(constraintBox(), re.glContext))
						.use(new DrawFunction()
								.vao(RectangleVertexArrayObject.instance())
								.glContext(re.glContext));
			});
		}
	}

	public ContainerContent fill(int colour) {
		fill = true;
		this.colour = colour;
		return this;
	}

}
