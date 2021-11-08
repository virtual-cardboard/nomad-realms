package context.loading;

import static context.game.visuals.shape.HexagonVertexArrayObject.createHexagonEBOLoadTask;
import static context.game.visuals.shape.HexagonVertexArrayObject.createHexagonVBOLoadTask;
import static context.visuals.lwjgl.ShaderType.FRAGMENT;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
import context.visuals.lwjgl.VertexArrayObject;
import context.visuals.lwjgl.VertexBufferObject;
import context.visuals.text.GameFont;
import loading.CardArtTextureLoadTask;
import loading.NomadRealmsFontLoadTask;
import loading.NomadRealmsShaderLoadTask;
import loading.NomadsTextureLoadTask;

public class LoadingGameVisuals extends GameVisuals {

	private int n = 0;
	boolean done;

	@Override
	public void init() {
		long time = System.currentTimeMillis();
		GameLoader loader = loader();
		ResourcePack rp = context().resourcePack();
		TransformationVertexShader transformationVS = rp.transformationVertexShader();
		TexturedTransformationVertexShader texturedTransformationVS = rp.texturedTransformationVertexShader();

		Future<ElementBufferObject> febo = loader.submit(createHexagonEBOLoadTask());
		Future<VertexBufferObject> fvbo = loader.submit(createHexagonVBOLoadTask());

		Future<Texture> fBaloo2Tex = loader.submit(new NomadsTextureLoadTask(n++, "fonts/baloo2.png"));

		Future<Shader> fHexagonFS = loader.submit(new NomadRealmsShaderLoadTask(FRAGMENT, "shaders/hexagonFragmentShader.glsl"));

		Future<Shader> fTextFS = loader.submit(new ShaderLoadTask(FRAGMENT, "shaders/textFragmentShader.glsl"));

		Future<Shader> fTextureFS = loader.submit(new ShaderLoadTask(FRAGMENT, "shaders/textureFragmentShader.glsl"));

		Future<Texture> fCardBackWood = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_back_wood.png"));
		Future<Texture> fCardBase = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_base.png"));
		Future<Texture> fCardDecorationAction = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_decoration_action.png"));
		Future<Texture> fCardDecorationCantrip = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_decoration_cantrip.png"));
		Future<Texture> fCardDecorationCreature = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_decoration_creature.png"));
		Future<Texture> fCardDecorationStructure = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_decoration_structure.png"));
		Future<Texture> fCardFront = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_front.png"));
		Future<Texture> fCardBanner = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_banner.png"));
		Future<Texture> fCardLogo = loader.submit(new NomadsTextureLoadTask(n++, "card_template/card_logo.png"));

		Future<Texture> fMeteor = loader.submit(new CardArtTextureLoadTask(n++, "meteor"));
		Future<Texture> fExtraPreparation = loader.submit(new CardArtTextureLoadTask(n++, "extra_preparation"));
		Future<Texture> fTeleport = loader.submit(new CardArtTextureLoadTask(n++, "teleport"));
		Future<Texture> fRegenesis = loader.submit(new CardArtTextureLoadTask(n++, "regenesis"));
		try {
			Texture baloo2Tex = fBaloo2Tex.get();
			Future<GameFont> fBaloo2Font = loader.submit(new NomadRealmsFontLoadTask("fonts/baloo2.vcfont", baloo2Tex));
			GameFont baloo2Font = fBaloo2Font.get();
			rp.putFont("baloo2", baloo2Font);

			ElementBufferObject ebo = febo.get();
			VertexBufferObject vbo = fvbo.get();
			Future<VertexArrayObject> fvao = loader.submit(new VertexArrayObjectLoadTask(new HexagonVertexArrayObject(), ebo, vbo));
			rp.putVAO("hexagon", fvao.get());

			Shader hexagonFS = fHexagonFS.get();
			HexagonShaderProgram hexagonSP = new HexagonShaderProgram(transformationVS, hexagonFS);
			loader.submit(new ShaderProgramLoadTask(hexagonSP)).get();
			rp.putShaderProgram("hexagon", hexagonSP);

			Shader textFS = fTextFS.get();
			TextShaderProgram textSP = new TextShaderProgram(texturedTransformationVS, textFS);
			loader.submit(new ShaderProgramLoadTask(textSP)).get();
			rp.putShaderProgram("text", textSP);

			Shader textureFS = fTextureFS.get();
			TextureShaderProgram textureSP = new TextureShaderProgram(texturedTransformationVS, textureFS);
			loader.submit(new ShaderProgramLoadTask(textureSP)).get();
			rp.putShaderProgram("texture", textureSP);

			rp.putTexture("card_back_wood", fCardBackWood.get());
			rp.putTexture("card_base", fCardBase.get());
			rp.putTexture("card_decoration_action", fCardDecorationAction.get());
			rp.putTexture("card_decoration_cantrip", fCardDecorationCantrip.get());
			rp.putTexture("card_decoration_creature", fCardDecorationCreature.get());
			rp.putTexture("card_decoration_structure", fCardDecorationStructure.get());
			rp.putTexture("card_front", fCardFront.get());
			rp.putTexture("card_banner", fCardBanner.get());
			rp.putTexture("card_logo", fCardLogo.get());

			rp.putTexture("meteor", fMeteor.get());
			rp.putTexture("extra_preparation", fExtraPreparation.get());
			rp.putTexture("teleport", fTeleport.get());
			rp.putTexture("regenesis", fRegenesis.get());
			System.out.println("Finished loading in " + (System.currentTimeMillis() - time) + "ms.");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		done = true;
	}

	@Override
	public void render() {
	}

}
