package context.game.visuals.gui;

import static context.visuals.colour.Colour.rgb;
import static context.visuals.colour.Colour.toRangedVector;

import java.util.List;

import common.math.Matrix4f;
import common.math.PosDim;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.ShaderProgram;

public class HandHolderGui extends CardZoneGui {

	private RectangleVertexArrayObject rectangleVAO;
	private ShaderProgram defaultShaderProgram;

	public HandHolderGui(ResourcePack resourcePack) {
		setWidth(new PixelDimensionConstraint(800));
		setHeight(new PixelDimensionConstraint(100));
		setPosX(new CenterPositionConstraint(getWidth()));
		setPosY(new PixelPositionConstraint(0, getHeight()));
		rectangleVAO = resourcePack.rectangleVAO();
		defaultShaderProgram = resourcePack.defaultShaderProgram();
	}

	@Override
	public void render(GLContext glContext, Matrix4f matrix4f, float x, float y, float width, float height) {
		defaultShaderProgram.bind();
		defaultShaderProgram.setMat4("matrix4f", matrix4f.translate(x, y).scale(width, height));
		defaultShaderProgram.setVec4("fill", toRangedVector(rgb(117, 96, 60)));
		rectangleVAO.draw(glContext);
	}

	@Override
	public void resetTargetPositions(Vector2f screenDimensions) {
		List<CardGui> cardGuis = cardGuis();
		PosDim posdim = posdim();
		float increment = (posdim.w - 80 - CardGui.WIDTH) / cardGuis.size();
		for (int i = 0; i < cardGuis.size(); i++) {
			CardGui cardGui = cardGuis.get(i);
			cardGui.setTargetPos(posdim.x + 40 + i * increment + CardGui.WIDTH * 0.5f, screenDimensions.y - CardGui.HEIGHT * 0.4f);
		}
	}

}
