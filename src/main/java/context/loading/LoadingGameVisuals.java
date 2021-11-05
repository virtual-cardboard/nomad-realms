package context.loading;

import static context.game.visuals.shape.HexagonVertexArrayObject.createHexagonEBOLoadTask;
import static context.game.visuals.shape.HexagonVertexArrayObject.createHexagonVBOLoadTask;
import static context.visuals.lwjgl.ShaderType.FRAGMENT;

import java.util.concurrent.ExecutionException;

import common.loader.GameLoader;
import common.loader.loadtask.ShaderLoadTask;
import common.loader.loadtask.ShaderProgramLoadTask;
import common.loader.loadtask.VertexArrayObjectLoadTask;
import context.ResourcePack;
import context.game.visuals.renderer.hexagon.HexagonShaderProgram;
import context.game.visuals.shape.HexagonVertexArrayObject;
import context.visuals.GameVisuals;
import context.visuals.builtin.TextShaderProgram;
import context.visuals.builtin.TextureShaderProgram;
import context.visuals.builtin.TexturedTransformationVertexShader;
import context.visuals.builtin.TransformationVertexShader;
import context.visuals.lwjgl.ElementBufferObject;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.Texture;
import context.visuals.lwjgl.VertexBufferObject;
import context.visuals.text.GameFont;
import loading.NomadRealmsFontLoadTask;
import loading.NomadRealmsShaderLoadTask;
import loading.NomadsTextureLoadTask;

public class LoadingGameVisuals extends GameVisuals {

	private int n = 0;
	boolean done;

	public LoadingGameVisuals() {
	}

	@Override
	public void render() {
		if (!done) {
			try {
				GameLoader loader = loader();
				ResourcePack rp = context().resourcePack();
				TransformationVertexShader transformationVS = rp.transformationVertexShader();
				TexturedTransformationVertexShader texturedTransformationVS = rp.texturedTransformationVertexShader();

				ElementBufferObject ebo = loader.submit(createHexagonEBOLoadTask()).get();
				VertexBufferObject vbo = loader.submit(createHexagonVBOLoadTask()).get();
				rp.putVAO("hexagon", loader.submit(new VertexArrayObjectLoadTask(new HexagonVertexArrayObject(), ebo, vbo)).get());

				Shader hexagonFS = loader.submit(new NomadRealmsShaderLoadTask(FRAGMENT, "shaders/hexagonFragmentShader.glsl")).get();
				HexagonShaderProgram hexagonSP = new HexagonShaderProgram(transformationVS, hexagonFS);
				loader.submit(new ShaderProgramLoadTask(hexagonSP)).get();
				rp.putShaderProgram("hexagon", hexagonSP);

				Shader textFS = loader.submit(new ShaderLoadTask(FRAGMENT, "shaders/textFragmentShader.glsl")).get();
				TextShaderProgram textSP = new TextShaderProgram(texturedTransformationVS, textFS);
				loader.submit(new ShaderProgramLoadTask(textSP)).get();
				rp.putShaderProgram("text", textSP);

				Shader textureFS = loader.submit(new ShaderLoadTask(FRAGMENT, "shaders/textureFragmentShader.glsl")).get();
				TextureShaderProgram textureSP = new TextureShaderProgram(texturedTransformationVS, textureFS);
				loader.submit(new ShaderProgramLoadTask(textureSP)).get();
				rp.putShaderProgram("texture", textureSP);

				rp.putTexture("card_base", loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_base.png")).get());
				rp.putTexture("card_decoration_action", loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_decoration_action.png")).get());
				rp.putTexture("card_decoration_cantrip", loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_decoration_cantrip.png")).get());
				rp.putTexture("card_decoration_creature", loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_decoration_creature.png")).get());
				rp.putTexture("card_decoration_structure", loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_decoration_structure.png")).get());
				rp.putTexture("card_front", loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_front.png")).get());
				rp.putTexture("card_banner", loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_banner.png")).get());

				Texture baloo2Tex = loader.submit(new NomadsTextureLoadTask(n++, "fonts/baloo2.png")).get();
				GameFont baloo2Font = loader.submit(new NomadRealmsFontLoadTask("fonts/baloo2.vcfont", baloo2Tex)).get();
				rp.putFont("baloo2", baloo2Font);

				rp.putTexture("meteor", loader.submit(new NomadsTextureLoadTask(n++, "card_art/meteor.png")).get());
				rp.putTexture("extra_preparation", loader.submit(new NomadsTextureLoadTask(n++, "card_art/foresight.png")).get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			done = true;
		}
	}

}
