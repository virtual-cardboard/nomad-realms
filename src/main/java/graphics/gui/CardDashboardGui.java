package graphics.gui;

import static context.visuals.colour.Colour.colour;
import static context.visuals.colour.Colour.toNormalizedVector;

import common.math.Matrix4f;
import context.GLContext;
import context.GameContext;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.lwjgl.ShaderProgram;
import context.visuals.lwjgl.VertexArrayObject;

public final class CardDashboardGui extends Gui {

	private ShaderProgram defaultShaderProgram;
	private VertexArrayObject rectangleVAO;
	private GLContext glContext;

	public CardDashboardGui(GameContext context) {
		defaultShaderProgram = context.resourcePack().defaultShaderProgram();
		rectangleVAO = context.resourcePack().rectangleVAO();
		glContext = context.glContext();

		PixelDimensionConstraint width = new PixelDimensionConstraint(800);
		setWidth(width);
		setHeight(new PixelDimensionConstraint(100));
		setPosX(new CenterPositionConstraint(width));
		setPosY(new BiFunctionPositionConstraint((start, end) -> end - 100));
	}

	@Override
	public void render(Matrix4f matrix4f, float x, float y, float width, float height) {
		matrix4f.translate(x, y).scale(width, height);
		defaultShaderProgram.bind();
		defaultShaderProgram.setMat4("matrix4f", matrix4f);
		defaultShaderProgram.setVec4("colour", toNormalizedVector(colour(117, 96, 60)));
		rectangleVAO.display(glContext);
	}

}
