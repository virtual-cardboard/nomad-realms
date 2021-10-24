package graphics.renderer;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GameContext;
import context.visuals.colour.Colour;
import context.visuals.gui.RootGui;
import context.visuals.lwjgl.ShaderProgram;
import context.visuals.lwjgl.VertexArrayObject;
import context.visuals.renderer.GameRenderer;

public class HexagonRenderer extends GameRenderer {

	private ShaderProgram shaderProgram;

	private VertexArrayObject hexagonVAO;

	public HexagonRenderer(GameContext context) {
		super(context);
		this.shaderProgram = resourcePack.getShaderProgram("hexagon");
		this.hexagonVAO = resourcePack.getVAO("hexagon");
	}

	public void render(RootGui rootGui, float x, float y, float width, float height, int colour) {
		Vector2f rootGuiDimensions = rootGui.getDimensions();
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / rootGuiDimensions.x, 1 / rootGuiDimensions.y);
		matrix4f.translate(x, y).scale(width, height);
		shaderProgram.bind();
		shaderProgram.setMat4("matrix4f", matrix4f);
		shaderProgram.setVec4("fill", Colour.toNormalizedVector(colour));
		hexagonVAO.display(glContext);
	}

}
