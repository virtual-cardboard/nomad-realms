package graphics.gui;

import common.math.Matrix4f;
import common.math.Vector4f;
import context.GLContext;
import context.GameContext;
import context.visuals.gui.Gui;
import context.visuals.lwjgl.ShaderProgram;
import context.visuals.lwjgl.VertexArrayObject;

public final class CardDashboardGui extends Gui {

	private ShaderProgram defaultShaderProgram;
	private VertexArrayObject rectangleVAO;
	private GLContext glContext;

	public CardDashboardGui(GameContext context) {
		defaultShaderProgram = context.resourcePack().defaultShaderProgram();
		rectangleVAO = context.resourcePack().rectangleVAO();
		glContext = context.glContext();
	}

	@Override
	public void render(Matrix4f matrix4f, float x, float y, float width, float height) {
		matrix4f.translate(x, y).scale(width, height);
		defaultShaderProgram.bind();
		defaultShaderProgram.setMat4("matrix4f", matrix4f);
		defaultShaderProgram.setVec4("colour", new Vector4f(1, 1, 1, 1));
		rectangleVAO.display(glContext);
	}

}
