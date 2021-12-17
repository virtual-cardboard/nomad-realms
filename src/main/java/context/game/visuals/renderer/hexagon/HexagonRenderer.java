package context.game.visuals.renderer.hexagon;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.game.visuals.shape.HexagonVertexArrayObject;
import context.visuals.gui.RootGui;
import context.visuals.renderer.GameRenderer;

public class HexagonRenderer extends GameRenderer {

	private HexagonShaderProgram shaderProgram;
	private HexagonVertexArrayObject vao;

	public HexagonRenderer(HexagonShaderProgram shaderProgram, HexagonVertexArrayObject vao) {
		this.shaderProgram = shaderProgram;
		this.vao = vao;
	}

	public void render(GLContext glContext, RootGui rootGui, float x, float y, float width, float height, int colour) {
		Vector2f rootGuiDimensions = rootGui.dimensions();
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / rootGuiDimensions.x, 1 / rootGuiDimensions.y);
		matrix4f.translate(x, y).scale(width, height);
		shaderProgram.bind(glContext);
		shaderProgram.setMat4("matrix4f", matrix4f);
		shaderProgram.setColour("fill", colour);
		vao.draw(glContext);
	}

	public void render(GLContext glContext, RootGui rootGui, float x, float y, float z, float width, float height, int colour) {
		Vector2f rootGuiDimensions = rootGui.dimensions();
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1, z).scale(2, -2).scale(1 / rootGuiDimensions.x, 1 / rootGuiDimensions.y);
		matrix4f.translate(x, y).scale(width, height);
		shaderProgram.bind(glContext);
		shaderProgram.setMat4("matrix4f", matrix4f);
		shaderProgram.setColour("fill", colour);
		vao.draw(glContext);
	}

}
