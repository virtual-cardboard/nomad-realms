package graphics.gui;

import static context.visuals.colour.Colour.colour;
import static context.visuals.colour.Colour.toNormalizedVector;

import common.math.Matrix4f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.Gui;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.BiFunctionPositionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.lwjgl.ShaderProgram;
import context.visuals.lwjgl.VertexArrayObject;
import model.card.CardDashboard;

public final class CardDashboardGui extends Gui {

	private ShaderProgram defaultShaderProgram;
	private VertexArrayObject rectangleVAO;
	private CardDashboard cardDashboard;

	public CardDashboardGui(ResourcePack resourcePack, CardDashboard cardDashboard) {
		defaultShaderProgram = resourcePack.defaultShaderProgram();
		rectangleVAO = resourcePack.rectangleVAO();
		this.cardDashboard = cardDashboard;
		PixelDimensionConstraint width = new PixelDimensionConstraint(800);
		setWidth(width);
		setHeight(new PixelDimensionConstraint(100));
		setPosX(new CenterPositionConstraint(width));
		setPosY(new BiFunctionPositionConstraint((start, end) -> end - 100));
	}

	@Override
	public void render(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
		renderDashboard(glContext, matrix4f, x, y, width, height);
//		CardZone hand = cardDashboard.hand();
//		for (int i = 0; i < hand.size(); i++) {
//			GameCard card = hand.card(i);
//			card.
//		}
	}

	private void renderDashboard(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
		matrix4f.translate(x, y).scale(width, height);
		defaultShaderProgram.bind();
		defaultShaderProgram.setMat4("matrix4f", matrix4f);
		defaultShaderProgram.setVec4("fill", toNormalizedVector(colour(117, 96, 60)));
		rectangleVAO.draw(glContext);
	}

}