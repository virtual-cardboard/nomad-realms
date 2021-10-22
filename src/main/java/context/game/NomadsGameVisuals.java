package context.game;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import context.visuals.GameVisuals;

public class NomadsGameVisuals extends GameVisuals {

	public static final float[] POSITIONS = {
			0.00f, 0.5f,
			0.25f, 1.0f,
			0.75f, 1.0f,
			1.00f, 0.5f,
			0.75f, 0.0f,
			0.25f, 0.0f
	};

	public static final int[] INDICES = {
			0, 1, 5,
			1, 5, 4,
			1, 2, 4,
			2, 3, 4
	};

	private static final float[] TEXTURE_COORDINATES = {
			1.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f,
			0.0f, 1.0f
	};

//	private static final int[] INDICES = {
//			0, 1, 2,
//			0, 2, 3
//	};

//	private HexagonRenderer hexagonRenderer;
	private boolean done;

	private int vao;

	private int ebo;

	private int shaderProgram;

	@Override
	protected void init() {
//		hexagonRenderer = new HexagonRenderer(context());
	}

	@Override
	public void render() {
		background(0.4f, 0.4f, 0.4f, 1);
//		hexagonRenderer.render();
//		NomadsGameData data = (NomadsGameData) context().data();
//		TileMap map = data.map();
//		for (int i = 0, h = map.height(); i < h; i++) {
//			for (int j = 0, w = map.width(); j < w; j++) {
//			}
//		}
		if (!done) {
			int vertexShader = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(vertexShader, "#version 330 core\r\n"
					+ "layout (location = 0) in vec2 vertexPos;\r\n"
					+ "void main() { gl_Position = vec4(vertexPos, 0, 1); }");
			glCompileShader(vertexShader);
			int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(fragmentShader, "#version 330 core\r\n"
					+ "out vec4 FragColor;\r\n"
					+ "void main() { FragColor = vec4(0.5f, 0.8f, 0, 1); }");
			glCompileShader(fragmentShader);

			shaderProgram = glCreateProgram();
			glAttachShader(shaderProgram, vertexShader);
			glAttachShader(shaderProgram, fragmentShader);
			glLinkProgram(shaderProgram);

			glDeleteShader(vertexShader);
			glDeleteShader(fragmentShader);

			vao = glGenVertexArrays();
			int vbo = glGenBuffers();
			ebo = glGenBuffers();

			glBindVertexArray(vao);

			glBindBuffer(GL_ARRAY_BUFFER, vbo);
			glBufferData(GL_ARRAY_BUFFER, POSITIONS, GL_STATIC_DRAW);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

			glBufferData(GL_ELEMENT_ARRAY_BUFFER, INDICES, GL_STATIC_DRAW);

			glVertexAttribPointer(0, 2, GL_FLOAT, false, 2 * Float.BYTES, 0);
			glEnableVertexAttribArray(0);
			glFinish();

			glBindVertexArray(vao);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glDrawElements(GL_TRIANGLES, 24, GL_UNSIGNED_INT, 0);
			done = true;

		}
//		hexagonVAO.display();
		glUseProgram(shaderProgram);
//		shaderProgram.setMat4("matrix4f", new Matrix4f());
//		shaderProgram.setVec4("colour", new Vector4f(0.3f, 0, 0.6f, 1));
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glDrawElements(GL_TRIANGLES, 24, GL_UNSIGNED_INT, 0);
	}

}
