package nomadrealms.render.ui.content;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;

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
			re.defaultShaderProgram
					.set("color", toRangedVector(rgb(100, 0, 0)))
					.set("transform", new Matrix4f(constraintBox(), re.glContext))
					.use(new DrawFunction()
							.vao(RectangleVertexArrayObject.instance())
							.glContext(re.glContext));
		}
	}

	public ContainerContent fill(int colour) {
		fill = true;
		this.colour = colour;
		return this;
	}

}
