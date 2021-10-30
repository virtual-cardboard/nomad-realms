package context.loading;

import static context.visuals.lwjgl.ShaderType.FRAGMENT;
import static graphics.shape.HexagonVertexArrayObject.createHexagonEBOLoadTask;
import static graphics.shape.HexagonVertexArrayObject.createHexagonVBOLoadTask;

import java.util.concurrent.ExecutionException;

import common.loader.loadtask.ShaderLoadTask;
import common.loader.loadtask.ShaderProgramLoadTask;
import common.loader.loadtask.VertexArrayObjectLoadTask;
import context.ResourcePack;
import context.visuals.GameVisuals;
import context.visuals.builtin.TextShaderProgram;
import context.visuals.builtin.TextureShaderProgram;
import context.visuals.builtin.TexturedTransformationVertexShader;
import context.visuals.builtin.TransformationVertexShader;
import context.visuals.lwjgl.ElementBufferObject;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.VertexBufferObject;
import graphics.renderer.hexagon.HexagonShaderProgram;
import graphics.shape.HexagonVertexArrayObject;
import loading.NomadRealmsTextureLoadTask;
import loading.NomadRealmsShaderLoadTask;

public class LoadingGameVisuals extends GameVisuals {

	boolean done;

	public LoadingGameVisuals() {
	}

	@Override
	public void render() {
		if (!done) {
			try {
				ResourcePack rp = context().resourcePack();
				TransformationVertexShader transformationVS = rp.transformationVertexShader();
				TexturedTransformationVertexShader texturedTransformationVS = rp.texturedTransformationVertexShader();

				ElementBufferObject ebo = loader().submit(createHexagonEBOLoadTask()).get();
				VertexBufferObject vbo = loader().submit(createHexagonVBOLoadTask()).get();
				rp.putVAO("hexagon", loader().submit(new VertexArrayObjectLoadTask(new HexagonVertexArrayObject(), ebo, vbo)).get());

				Shader hexagonFS = loader().submit(new NomadRealmsShaderLoadTask(FRAGMENT, "shaders/hexagonFragmentShader.glsl")).get();
				HexagonShaderProgram hexagonSP = new HexagonShaderProgram(transformationVS, hexagonFS);
				loader().submit(new ShaderProgramLoadTask(hexagonSP)).get();
				rp.putShaderProgram("hexagon", hexagonSP);

				Shader textFS = loader().submit(new ShaderLoadTask(FRAGMENT, "shaders/textFragmentShader.glsl")).get();
				TextShaderProgram textSP = new TextShaderProgram(texturedTransformationVS, textFS);
				loader().submit(new ShaderProgramLoadTask(textSP)).get();
				rp.putShaderProgram("text", textSP);

				Shader textureFS = loader().submit(new ShaderLoadTask(FRAGMENT, "shaders/textureFragmentShader.glsl")).get();
				TextureShaderProgram textureSP = new TextureShaderProgram(texturedTransformationVS, textureFS);
				loader().submit(new ShaderProgramLoadTask(textureSP)).get();
				rp.putShaderProgram("texture", textureSP);

				rp.putTexture("card_base", loader().submit(new NomadRealmsTextureLoadTask(0, "card_template/card_base.png")).get());
				rp.putTexture("card_banner", loader().submit(new NomadRealmsTextureLoadTask(1, "card_template/card_banner.png")).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			done = true;
		}
	}

}
