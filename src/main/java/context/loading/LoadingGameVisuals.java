package context.loading;

import static context.game.visuals.displayable.HexagonVertexArrayObject.createHexagonEBOLoadTask;
import static context.game.visuals.displayable.HexagonVertexArrayObject.createHexagonVBOLoadTask;
import static context.visuals.lwjgl.ShaderType.FRAGMENT;
import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import common.loader.loadtask.EmptyTextureLoadTask;
import common.loader.loadtask.FrameBufferObjectLoadTask;
import common.loader.loadtask.ShaderLoadTask;
import common.loader.loadtask.ShaderProgramLoadTask;
import common.loader.loadtask.VertexArrayObjectLoadTask;
import context.ResourcePack;
import context.game.visuals.displayable.HexagonVertexArrayObject;
import context.game.visuals.renderer.ActorBodyPartRenderer;
import context.game.visuals.renderer.ParticleRenderer;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.game.visuals.renderer.hexagon.HexagonShaderProgram;
import context.visuals.GameVisuals;
import context.visuals.builtin.*;
import context.visuals.lwjgl.*;
import context.visuals.renderer.LineRenderer;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import loading.NomadRealmsFontLoadTask;
import loading.NomadRealmsShaderLoadTask;
import loading.NomadsTextureLoadTask;

public class LoadingGameVisuals extends GameVisuals {

	boolean done;

	@Override
	public void init() {
		loadResources();
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_ALWAYS);
		done = true;
	}

	private void loadResources() {
		long time = System.currentTimeMillis();

		ResourcePack rp = resourcePack();
		TransformationVertexShader transformationVS = rp.transformationVertexShader();
		TexturedTransformationVertexShader texturedTransformationVS = rp.texturedTransformationVertexShader();

		// EBOs and VBOs
		Future<ElementBufferObject> febo = loader().submit(createHexagonEBOLoadTask());
		Future<VertexBufferObject> fvbo = loader().submit(createHexagonVBOLoadTask());

		// Font textures
		Future<Texture> fBaloo2Tex = loader().submit(new NomadsTextureLoadTask("fonts/baloo2.png"));
		Future<Texture> fLangarTex = loader().submit(new NomadsTextureLoadTask("fonts/langar.png"));

		// Shaders
		Future<Shader> fHexagonFS = loader().submit(new NomadRealmsShaderLoadTask(FRAGMENT, "shaders/hexagonFragmentShader.glsl"));
		Future<Shader> fTextFS = loader().submit(new ShaderLoadTask(FRAGMENT, "shaders/textFragmentShader.glsl"));
		Future<Shader> fTextureFS = loader().submit(new ShaderLoadTask(FRAGMENT, "shaders/textureFragmentShader.glsl"));
		Future<Shader> fLineFS = loader().submit(new ShaderLoadTask(FRAGMENT, "shaders/lineFragmentShader.glsl"));

		Map<String, String> texMap = new HashMap<>();
		texMap.put("queue_gui", "gui/queue_gui.png");

		texMap.put("card_back_wood", "card_template/card_back_wood.png");
		texMap.put("card_base", "card_template/card_base.png");
		texMap.put("card_decoration_action", "card_template/card_decoration_action.png");
		texMap.put("card_decoration_cantrip", "card_template/card_decoration_cantrip.png");
		texMap.put("card_decoration_creature", "card_template/card_decoration_creature.png");
		texMap.put("card_decoration_structure", "card_template/card_decoration_structure.png");
		texMap.put("card_decoration_task", "card_template/card_decoration_structure.png"); // TODO make card_decoration_task.png
		texMap.put("card_front", "card_template/card_front.png");
		texMap.put("card_banner", "card_template/card_banner.png");

		texMap.put("chain_segment", "effect_chain/chain_segment.png");
		texMap.put("effect_square", "effect_chain/effect_square.png");

		texMap.put("meteor", "card_art/meteor.png");
		texMap.put("extra_preparation", "card_art/extra_preparation.png");
		texMap.put("zap", "card_art/zap.png");
		texMap.put("test_task", "card_art/zap.png");// TODO
		texMap.put("teleport", "card_art/teleport.png");
		texMap.put("regenesis", "card_art/regenesis.png");
		texMap.put("overclocked_machinery", "card_art/meteor.png");// TODO

		texMap.put("health", "actor/health.png");
		texMap.put("nomad_body", "actor/nomad_body.png");
		texMap.put("nomad_head", "actor/nomad_head.png");

		texMap.put("tiny_toad", "actor/tiny_toad.png");

		texMap.put("logo_large", "logo/logo_large.png");
		texMap.put("logo_small", "logo/logo_small.png");
		texMap.put("logo_placeholder", "logo/logo_placeholder.png");

		texMap.put("circle_particle", "particles/circle.png");
		texMap.put("hexagon_particle", "particles/hexagon.png");
		texMap.put("card_particle", "particles/card.png");
		Map<String, Future<Texture>> fTexMap = new HashMap<>();
		texMap.forEach((name, path) -> fTexMap.put(name, loader().submit(new NomadsTextureLoadTask(path))));

		try {
			ElementBufferObject ebo = febo.get();
			VertexBufferObject vbo = fvbo.get();
			Future<VertexArrayObject> fvao = loader().submit(new VertexArrayObjectLoadTask(new HexagonVertexArrayObject(), ebo, vbo));
			rp.putVAO("hexagon", fvao.get());

			int w = 300;
			int h = 200;
			Texture textTexture = loader().submit(new EmptyTextureLoadTask(w, h)).get();
			FrameBufferObject textFBO = loader().submit(new FrameBufferObjectLoadTask(textTexture, null)).get();
			rp.putFBO("text", textFBO);

			Texture baloo2Tex = fBaloo2Tex.get();
			Future<GameFont> fBaloo2Font = loader().submit(new NomadRealmsFontLoadTask("fonts/baloo2.vcfont", baloo2Tex));
			GameFont baloo2Font = fBaloo2Font.get();
			rp.putFont("baloo2", baloo2Font);

			Texture langarTex = fLangarTex.get();
			Future<GameFont> fLangarFont = loader().submit(new NomadRealmsFontLoadTask("fonts/langar.vcfont", langarTex));
			GameFont langarFont = fLangarFont.get();
			rp.putFont("langar", langarFont);

			Shader hexagonFS = fHexagonFS.get();
			HexagonShaderProgram hexagonSP = new HexagonShaderProgram(transformationVS, hexagonFS);
			loader().submit(new ShaderProgramLoadTask(hexagonSP)).get();
			rp.putShaderProgram("hexagon", hexagonSP);

			Shader textFS = fTextFS.get();
			TextShaderProgram textSP = new TextShaderProgram(texturedTransformationVS, textFS);
			loader().submit(new ShaderProgramLoadTask(textSP)).get();
			rp.putShaderProgram("text", textSP);

			Shader textureFS = fTextureFS.get();
			TextureShaderProgram textureSP = new TextureShaderProgram(texturedTransformationVS, textureFS);
			loader().submit(new ShaderProgramLoadTask(textureSP)).get();
			rp.putShaderProgram("texture", textureSP);

			Shader lineFS = fLineFS.get();
			LineShaderProgram lineSP = new LineShaderProgram(transformationVS, lineFS);
			loader().submit(new ShaderProgramLoadTask(lineSP)).get();
			rp.putShaderProgram("line", lineSP);

			RectangleVertexArrayObject rectangleVAO = rp.rectangleVAO();

			HexagonVertexArrayObject hexagonVAO = (HexagonVertexArrayObject) rp.getVAO("hexagon");
			HexagonRenderer hexagonRenderer = new HexagonRenderer(hexagonSP, hexagonVAO);
			rp.putRenderer("hexagon", hexagonRenderer);

			TextureRenderer textureRenderer = new TextureRenderer(textureSP, rectangleVAO);
			rp.putRenderer("texture", textureRenderer);

			TextRenderer textRenderer = new TextRenderer(textureRenderer, textSP, rectangleVAO, rp.getFBO("text"));
			rp.putRenderer("text", textRenderer);

			LineRenderer lineRenderer = new LineRenderer(lineSP, rectangleVAO);
			rp.putRenderer("line", lineRenderer);

			ParticleRenderer particleRenderer = new ParticleRenderer(textureRenderer, lineRenderer);
			rp.putRenderer("particle", particleRenderer);

			ActorBodyPartRenderer bodyPartRenderer = new ActorBodyPartRenderer(textureRenderer, lineRenderer);
			rp.putRenderer("actor_body_part", bodyPartRenderer);

			rp.putRenderer("rectangle", new RectangleRenderer(rp.defaultShaderProgram(), rectangleVAO));

			fTexMap.forEach((name, fTexture) -> {
				try {
					rp.putTexture(name, fTexture.get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			});

			System.out.println("Nomad Realms finished loading in " + (System.currentTimeMillis() - time) + "ms.");
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render() {
	}

}
