package context.game.visuals.renderer.hexagon;

import context.visuals.lwjgl.VertexArrayObject;
import context.visuals.renderer.GameRenderer;
import engine.common.math.Matrix4f;

public class HexagonRenderer extends GameRenderer {

	private HexagonShaderProgram shaderProgram;
	private VertexArrayObject vao;

	public HexagonRenderer(HexagonShaderProgram shaderProgram, VertexArrayObject vao) {
		this.shaderProgram = shaderProgram;
		this.vao = vao;
	}

	public void render(float x, float y, float width, float height, int colour) {
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1).scale(2, -2).scale(1 / glContext.width(), 1 / glContext.height());
		matrix4f.translate(x, y).scale(width, height);
		shaderProgram.bind(glContext);
		shaderProgram.setMat4("matrix4f", matrix4f);
		shaderProgram.setColour("fill", colour);
		vao.draw(glContext);
	}

	public void render(float x, float y, float z, float width, float height, int colour) {
		Matrix4f matrix4f = new Matrix4f();
		matrix4f.translate(-1, 1, z).scale(2, -2).scale(1 / glContext.width(), 1 / glContext.height());
		matrix4f.translate(x, y).scale(width, height);
		shaderProgram.bind(glContext);
		shaderProgram.setMat4("matrix4f", matrix4f);
		shaderProgram.setColour("fill", colour);
		vao.draw(glContext);
	}

}
