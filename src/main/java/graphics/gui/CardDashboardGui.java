package graphics.gui;

import static context.visuals.colour.Colour.colour;
import static context.visuals.colour.Colour.toNormalizedVector;

import java.util.List;
import java.util.stream.Collectors;

import common.math.Matrix4f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.lwjgl.ShaderProgram;
import context.visuals.lwjgl.VertexArrayObject;

public final class CardDashboardGui extends Gui {

	private ShaderProgram defaultShaderProgram;
	private VertexArrayObject rectangleVAO;

	public CardDashboardGui(ResourcePack resourcePack) {
		defaultShaderProgram = resourcePack.defaultShaderProgram();
		rectangleVAO = resourcePack.rectangleVAO();
		setWidth(new PixelDimensionConstraint(800));
		setHeight(new PixelDimensionConstraint(200));
		setPosX(new CenterPositionConstraint(getWidth()));
		setPosY(new BiFunctionPositionConstraint((start, end) -> end - 200));
	}

	@Override
	public void render(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
		renderDashboard(glContext, matrix4f, x, y + height / 2, width, height / 2);
	}

	private void renderDashboard(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
		matrix4f.translate(x, y).scale(width, height);
		defaultShaderProgram.bind();
		defaultShaderProgram.setMat4("matrix4f", matrix4f);
		defaultShaderProgram.setVec4("fill", toNormalizedVector(colour(117, 96, 60)));
		rectangleVAO.draw(glContext);
	}

	@Override
	public void addChild(Gui child) {
		if (child instanceof CardGui) {
			super.addChild(child);
			return;
		}
		throw new RuntimeException("Gui " + child.getClass().getSimpleName() + " cannot be a child of CardDashboardGui.");
	}

	public List<CardGui> cardGuis() {
		return getChildren().stream().map(child -> (CardGui) child).collect(Collectors.toList());
	}

}