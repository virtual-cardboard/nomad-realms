package graphics.renderer;

import common.math.Matrix4f;
import common.math.Vector4f;
import context.GameContext;
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

	public void render() {
		shaderProgram.bind();
		shaderProgram.setMat4("matrix4f", new Matrix4f());
		shaderProgram.setVec4("fill", new Vector4f(0.3f, 0, 0.6f, 1));
		hexagonVAO.display(glContext);
	}

}
