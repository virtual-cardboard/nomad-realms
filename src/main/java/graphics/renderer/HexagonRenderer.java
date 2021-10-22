package graphics.renderer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glFinish;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import context.GameContext;
import context.visuals.lwjgl.VertexArrayObject;
import context.visuals.renderer.GameRenderer;

public class HexagonRenderer extends GameRenderer {
	private static final float[] POSITIONS = {
			1.0f, 1.0f, 0.0f,
			1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 0.0f,
			0.0f, 1.0f, 0.0f
	};

	private static final float[] TEXTURE_COORDINATES = {
			1.0f, 1.0f,
			1.0f, 0.0f,
			0.0f, 0.0f,
			0.0f, 1.0f
	};

	private static final int[] INDICES = {
			0, 1, 2,
			0, 2, 3
	};
//	private ShaderProgram shaderProgram;
	private VertexArrayObject hexagonVAO;

	private GameContext context;

	private boolean done;

	private int vao;

	private int ebo;

	private int shaderProgram;

	public HexagonRenderer(GameContext context) {
		super(context);
		this.context = context;
//		this.shaderProgram = resourcePack().getShaderProgram("default");
//		this.hexagonVAO = resourcePack().rectangleVAO();
	}

	public void render() {
		if (!done) {
//			ElementBufferObject ebo = new ElementBufferObject(context.glContext(), INDICES);
//			VertexBufferObject positionsVBO = new VertexBufferObject(context.glContext(), POSITIONS, 3);
//			VertexBufferObject textureCoordinatesVBO = new VertexBufferObject(context.glContext(), TEXTURE_COORDINATES, 2);
//			VertexArrayObject vao = new VertexArrayObject(context.glContext());
//			vao.generateId();
//			ebo.generateId();
//			System.out.println(context.glContext().eboID);
//			System.out.println(glGenBuffers());
//			System.out.println(glGenBuffers());
//			ebo.loadData();
//			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo.getId());
//			glBufferData(GL_ELEMENT_ARRAY_BUFFER, INDICES, GL_STATIC_DRAW);
//			System.out.println(context.glContext().eboID);
//			System.out.println();
//			vao.setEbo(ebo);
//			positionsVBO.generateId();
//			positionsVBO.loadData();
//			vao.attachVBO(positionsVBO);
//			textureCoordinatesVBO.generateId();
//			textureCoordinatesVBO.loadData();
//			vao.attachVBO(textureCoordinatesVBO);
//			vao.enableVertexAttribPointers();
//			hexagonVAO = vao;
//			this.hexagonVAO = resourcePack().rectangleVAO();
//			this.hexagonVAO = RectangleVertexArrayObject.createRectangleVAO(context.glContext());

			int vertexShader = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(vertexShader, "#version 330 core\r\n"
					+ "layout (location = 0) in vec3 vertexPos;\r\n"
					+ "void main() { gl_Position = vec4(vertexPos, 1); }");
			glCompileShader(vertexShader);
			int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(fragmentShader, "#version 330 core\r\n"
					+ "out vec4 fragColor;\r\n"
					+ "void main() { fragColor = vec4(0.5f, 0.8f, 0, 1); }");
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
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, INDICES, ebo);

			glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
			glEnableVertexAttribArray(0);
			glFinish();

			glBindVertexArray(vao);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			done = true;

		}
//		hexagonVAO.display();
		glUseProgram(shaderProgram);
//		shaderProgram.setMat4("matrix4f", new Matrix4f());
//		shaderProgram.setVec4("colour", new Vector4f(0.3f, 0, 0.6f, 1));
		glBindVertexArray(vao);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
	}

}
