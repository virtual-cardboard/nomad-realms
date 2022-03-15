package context.game.visuals.gui.dashboard;

import static context.visuals.colour.Colour.rgb;
import static context.visuals.colour.Colour.toRangedVector;

import java.util.List;

import app.NomadsSettings;
import common.math.PosDim;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.gui.CardGui;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.CenterPositionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.ShaderProgram;
import model.state.GameState;

public class HandGui extends CardZoneGui {

	private RectangleVertexArrayObject rectangleVAO;
	private ShaderProgram defaultShaderProgram;

	public HandGui(ResourcePack resourcePack) {
		setWidth(new PixelDimensionConstraint(800));
		setHeight(new PixelDimensionConstraint(100));
		setPosX(new CenterPositionConstraint(width()));
		setPosY(new PixelPositionConstraint(0, height()));
		rectangleVAO = resourcePack.rectangleVAO();
		defaultShaderProgram = resourcePack.defaultShaderProgram();
	}

	@Override
	public void render(GLContext glContext, NomadsSettings s, GameState state, float x, float y, float width, float height) {
		defaultShaderProgram.bind(glContext);
		defaultShaderProgram.setMat4("matrix4f", rectToPixelMatrix4f(glContext.windowDim()).translate(x, y).scale(width, height));
		defaultShaderProgram.setVec4("fill", toRangedVector(rgb(117, 96, 60)));
		rectangleVAO.draw(glContext);
	}

	@Override
	public void resetTargetPositions(Vector2f screenDimensions, NomadsSettings settings) {
		PosDim posdim = posdim();
		List<CardGui> cardGuis = cardGuis();
		float size = cardGuis.size();
		float increment = (posdim.w + 10) / (size + 1);
		float start = posdim.x + (posdim.w - (size - 1) * increment) * 0.5f;
		for (int i = 0; i < size; i++) {
			CardGui cardGui = cardGuis.get(i);
			if (!cardGui.lockedTargetPos()) {
				cardGui.setTargetPos(start + i * increment, screenDimensions.y - settings.cardHeight() * 0.3f);
			}
		}
	}

}
