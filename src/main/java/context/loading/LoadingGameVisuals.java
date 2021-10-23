package context.loading;

import static context.visuals.lwjgl.ShaderType.FRAGMENT;
import static context.visuals.lwjgl.ShaderType.VERTEX;
import static graphics.shape.HexagonVertexArrayObject.createHexagonEBOLoadTask;
import static graphics.shape.HexagonVertexArrayObject.createHexagonVBOLoadTask;

import java.util.concurrent.ExecutionException;

import common.loader.loadtask.ShaderLoadTask;
import common.loader.loadtask.ShaderProgramLoadTask;
import common.loader.loadtask.VertexArrayObjectLoadTask;
import context.visuals.GameVisuals;
import context.visuals.lwjgl.ElementBufferObject;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.ShaderProgram;
import context.visuals.lwjgl.ShaderType;
import context.visuals.lwjgl.VertexBufferObject;
import graphics.shape.HexagonVertexArrayObject;

public class LoadingGameVisuals extends GameVisuals {

	boolean done;

	public LoadingGameVisuals() {
	}

	@Override
	public void render() {
		if (!done) {
			try {
				ElementBufferObject ebo = loader().submit(createHexagonEBOLoadTask()).get();
				VertexBufferObject vbo = loader().submit(createHexagonVBOLoadTask()).get();
				Shader vertexShader = loader().submit(createShaderLoadTask(VERTEX, "shaders/hexagonVertexShader.glsl")).get();
				Shader fragmentShader = loader().submit(createShaderLoadTask(FRAGMENT, "shaders/hexagonFragmentShader.glsl")).get();
				ShaderProgram hexagonShaderProgram = loader().submit(new ShaderProgramLoadTask(vertexShader, fragmentShader)).get();
				context().resourcePack().putShaderProgram("hexagon", hexagonShaderProgram);
				context().resourcePack().putVAO("hexagon", loader().submit(new VertexArrayObjectLoadTask(ebo, vbo)).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

			done = true;
		}
	}

	private ShaderLoadTask createShaderLoadTask(ShaderType type, String relativePath) {
		return new ShaderLoadTask(type, HexagonVertexArrayObject.class.getProtectionDomain().getCodeSource().getLocation().getPath() + relativePath);
	}

}
