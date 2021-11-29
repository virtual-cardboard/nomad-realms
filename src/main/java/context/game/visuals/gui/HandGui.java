package context.game.visuals.gui;

import static context.game.visuals.gui.CardGui.HEIGHT;
import static context.visuals.colour.Colour.rgb;
import static context.visuals.colour.Colour.toRangedVector;

import common.math.PosDim;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.ShaderProgram;

public class HandGui extends CardZoneGui {

	private RectangleVertexArrayObject rectangleVAO;
	private ShaderProgram defaultShaderProgram;

	public HandGui(ResourcePack resourcePack) {
		setWidth(new PixelDimensionConstraint(800));
		setHeight(new PixelDimensionConstraint(100));
		setPosX(new CenterPositionConstraint(getWidth()));
		setPosY(new PixelPositionConstraint(0, getHeight()));
		rectangleVAO = resourcePack.rectangleVAO();
		defaultShaderProgram = resourcePack.defaultShaderProgram();
	}

	@Override
	public void render(GLContext glContext, Vector2f screenDim, float x, float y, float width, float height) {
		defaultShaderProgram.bind();
		defaultShaderProgram.setMat4("matrix4f", rectToPixelMatrix4f(screenDim).translate(x, y).scale(width, height));
		defaultShaderProgram.setVec4("fill", toRangedVector(rgb(117, 96, 60)));
		rectangleVAO.draw(glContext);
	}

	@Override
	public void resetTargetPositions(Vector2f screenDimensions) {
		PosDim posdim = posdim();
		float size = cardGuis.size();
		float increment = (posdim.w + 10) / (size + 1);
		float start = posdim.x + (posdim.w - (size - 1) * increment) * 0.5f;
		for (int i = 0; i < size; i++) {
			CardGui cardGui = cardGuis.get(i);
			if (!cardGui.lockedTargetPos()) {
				cardGui.setTargetPos(start + i * increment, screenDimensions.y - HEIGHT * 0.1f);
			}
		}
	}

}
