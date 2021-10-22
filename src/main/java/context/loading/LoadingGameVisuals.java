package context.loading;

import common.loader.loadtask.ShaderLoadTask;
import context.visuals.GameVisuals;
import context.visuals.lwjgl.ShaderType;
import graphics.shape.HexagonVertexArrayObject;

public class LoadingGameVisuals extends GameVisuals {

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

	boolean done;

	public LoadingGameVisuals() {
	}

	@Override
	public void render() {
		if (!done) {
//			try {
//				ElementBufferObject ebo = loader().submit(createHexagonEBOLoadTask(context().glContext())).get();
//				VertexBufferObject vbo = loader().submit(createHexagonVBOLoadTask(context().glContext())).get();

//				Shader vertexShader = loader().submit(createShaderLoadTask(VERTEX, "shaders" + separator + "hexagonVertexShader.glsl")).get();
//				Shader fragmentShader = loader().submit(createShaderLoadTask(FRAGMENT, "shaders" + separator + "hexagonFragmentShader.glsl")).get();
//				ShaderProgram hexagonShaderProgram = loader().submit(new ShaderProgramLoadTask(vertexShader, fragmentShader)).get();
//				context().resourcePack().putShaderProgram("hexagon", hexagonShaderProgram);
//				context().resourcePack().putVAO("hexagon", loader().submit(new VertexArrayObjectLoadTask(context().glContext(), ebo, vbo)).get());
//			} catch (InterruptedException | ExecutionException e) {
//				e.printStackTrace();
//			}
//
//			ElementBufferObject ebo = new ElementBufferObject(context().glContext(), INDICES);
//			VertexBufferObject positionsVBO = new VertexBufferObject(context().glContext(), POSITIONS, 3);
//			VertexBufferObject textureCoordinatesVBO = new VertexBufferObject(context().glContext(), TEXTURE_COORDINATES, 2);
//			VertexArrayObject vao = new VertexArrayObject(context().glContext());
//			vao.generateId();
//			ebo.generateId();
//			ebo.loadData();
//			vao.setEbo(ebo);
//			positionsVBO.generateId();
//			positionsVBO.loadData();
//			vao.attachVBO(positionsVBO);
//			textureCoordinatesVBO.generateId();
//			textureCoordinatesVBO.loadData();
//			vao.attachVBO(textureCoordinatesVBO);
//			vao.enableVertexAttribPointers();
//
//			context().resourcePack().putVAO("hexagon", vao);

			done = true;
		}
	}

	private ShaderLoadTask createShaderLoadTask(ShaderType type, String relativePath) {
		return new ShaderLoadTask(type, HexagonVertexArrayObject.class.getProtectionDomain().getCodeSource().getLocation().getPath() + relativePath);
	}

}
