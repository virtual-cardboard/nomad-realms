package context.loading;

import static context.visuals.lwjgl.ShaderType.FRAGMENT;
import static graphics.shape.HexagonVertexArrayObject.createHexagonEBOLoadTask;
import static graphics.shape.HexagonVertexArrayObject.createHexagonVBOLoadTask;

import java.util.concurrent.ExecutionException;

import common.loader.loadtask.ShaderProgramLoadTask;
import common.loader.loadtask.VertexArrayObjectLoadTask;
import context.visuals.GameVisuals;
import context.visuals.lwjgl.ElementBufferObject;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.VertexBufferObject;
import graphics.renderer.hexagon.HexagonShaderProgram;
import graphics.shape.HexagonVertexArrayObject;
import loading.NomadsShaderLoadTask;

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
				Shader hexagonFS = loader().submit(new NomadsShaderLoadTask(FRAGMENT, "shaders/hexagonFragmentShader.glsl")).get();
				HexagonShaderProgram hexagonSP = new HexagonShaderProgram(context().resourcePack().transformationVertexShader(), hexagonFS);
				loader().submit(new ShaderProgramLoadTask(hexagonSP)).get();
				context().resourcePack().putShaderProgram("hexagon", hexagonSP);
				context().resourcePack().putVAO("hexagon", loader().submit(new VertexArrayObjectLoadTask(new HexagonVertexArrayObject(), ebo, vbo)).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

			done = true;
		}
	}

}
