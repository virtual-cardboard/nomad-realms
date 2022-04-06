package context.game.visuals.renderer;

import static context.visuals.colour.Colour.toRangedVector;

import common.math.Matrix4f;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.lwjgl.ShaderProgram;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.GameRenderer;

public class FadedTextureRenderer extends GameRenderer {

	private RectangleVertexArrayObject vao;
	private ShaderProgram shaderProgram;
	private float threshold = 0.5f;
	private int bright = 0;
	private int dark = 255;

	public FadedTextureRenderer(ShaderProgram fadedSP, RectangleVertexArrayObject vao) {
		this.vao = vao;
		this.shaderProgram = fadedSP;
	}

	public void render(Texture texture, Matrix4f matrix4f) {
		shaderProgram.bind(glContext);
		texture.bind(glContext, 0);
		shaderProgram.setMat4("matrix4f", matrix4f);
		shaderProgram.setInt("textureSampler", 0);
		shaderProgram.setFloat("threshold", threshold);
		shaderProgram.setVec4("bright", toRangedVector(bright));
		shaderProgram.setVec4("dark", toRangedVector(dark));
		vao.draw(glContext);
	}

}
