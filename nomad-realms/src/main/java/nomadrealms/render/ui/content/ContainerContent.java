package nomadrealms.render.ui.content;

import static engine.common.colour.Colour.toRangedVector;

import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.render.RenderingEnvironment;

public class ContainerContent extends BasicUIContent {

	private boolean fill = false;
	private int colour;
	private float radius = 0;
	private int borderColor = 0;
	private float borderWidth = 0;

	public ContainerContent(UIContent parent) {
		super(parent);
	}

	public ContainerContent(UIContent parent, ConstraintBox box) {
		super(parent, box);
	}

	@Override
	public void _render(RenderingEnvironment re) {
		if (fill) {
			re.rectangleRenderer.render(constraintBox(), radius, colour, borderColor, borderWidth);
		}
	}

	@Override
	public ContainerContent constraintBox(ConstraintBox box) {
		super.constraintBox(box);
		return this;
	}

	public ContainerContent fill(int colour) {
		return fill(colour, 0, 0, 0);
	}

	public ContainerContent fill(int colour, float radius) {
		return fill(colour, radius, 0, 0);
	}

	public ContainerContent fill(int colour, float radius, int borderColor, float borderWidth) {
		fill = true;
		this.colour = colour;
		this.radius = radius;
		this.borderColor = borderColor;
		this.borderWidth = borderWidth;
		return this;
	}

}
