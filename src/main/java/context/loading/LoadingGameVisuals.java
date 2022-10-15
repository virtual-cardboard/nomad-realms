package context.loading;

import static context.visuals.lwjgl.ShaderType.FRAGMENT;
import static graphics.displayable.HexagonVertexArrayObject.createHexagonEBOLoadTask;
import static graphics.displayable.HexagonVertexArrayObject.createHexagonVBOLoadTask;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import context.ResourcePack;
import context.game.visuals.renderer.ChainHeapRenderer;
import context.game.visuals.renderer.ParticleRenderer;
import context.game.visuals.renderer.hexagon.HexagonRenderer;
import context.game.visuals.renderer.hexagon.HexagonShaderProgram;
import context.visuals.GameVisuals;
import context.visuals.builtin.EllipseShaderProgram;
import context.visuals.builtin.LineShaderProgram;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.builtin.RectangleVertexArrayObject;
import context.visuals.builtin.TextShaderProgram;
import context.visuals.builtin.TextureShaderProgram;
import context.visuals.builtin.TexturedTransformationVertexShader;
import context.visuals.builtin.TransformationVertexShader;
import context.visuals.gui.renderer.RootGuiRenderer;
import context.visuals.lwjgl.ElementBufferObject;
import context.visuals.lwjgl.FrameBufferObject;
import context.visuals.lwjgl.Model;
import context.visuals.lwjgl.Shader;
import context.visuals.lwjgl.Texture;
import context.visuals.lwjgl.VertexArrayObject;
import context.visuals.lwjgl.VertexBufferObject;
import context.visuals.renderer.EllipseRenderer;
import context.visuals.renderer.LineRenderer;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import engine.common.loader.loadtask.EmptyTextureLoadTask;
import engine.common.loader.loadtask.FrameBufferObjectLoadTask;
import engine.common.loader.loadtask.ShaderLoadTask;
import engine.common.loader.loadtask.ShaderProgramLoadTask;
import engine.common.loader.loadtask.VertexArrayObjectLoadTask;
import loading.NomadRealmsFontLoadTask;
import loading.NomadRealmsModelLoadTask;
import loading.NomadRealmsShaderLoadTask;
import loading.NomadsTextureLoadTask;

public class LoadingGameVisuals extends GameVisuals {

	boolean done;

	@Override
	public void init() {
		loadResources();
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);
//		glEnable(GL_DEPTH_CLAMP);
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
		Future<Shader> fEllipseFS = loader().submit(new ShaderLoadTask(FRAGMENT, "shaders/ellipseFragmentShader.glsl"));

		Map<String, String> texMap = new HashMap<>();
		texMap.put("gui_queue", "gui/queue_gui.png");
		texMap.put("gui_start_button_outline", "gui/start_button_outline.png");
		texMap.put("gui_main_menu_background", "gui/main_menu_background.png");
		texMap.put("gui_menu_background", "gui/menu_background.png");

		texMap.put("card_back_wood", "card_template/card_back_wood.png");

		texMap.put("card_banner", "card_template/v2/banner.png");
		texMap.put("card_base", "card_template/v2/base.png");
		texMap.put("card_decoration_action", "card_template/v2/card_decoration_action.png");
		texMap.put("card_decoration_cantrip", "card_template/v2/card_decoration_cantrip.png");
		texMap.put("card_decoration_structure", "card_template/v2/card_decoration_structure.png");
		texMap.put("card_decoration_task", "card_template/v2/card_decoration_task.png");
		texMap.put("card_header", "card_template/v2/header.png");
		texMap.put("card_picture_frame", "card_template/v2/picture_frame.png");
		texMap.put("card_ribbon_left", "card_template/v2/ribbon_left.png");
		texMap.put("card_ribbon_right", "card_template/v2/ribbon_right.png");
		texMap.put("card_text_box", "card_template/v2/text_box.png");

//		texMap.put("card_front", "card_template/card_front.png");
//		texMap.put("card_banner", "card_template/card_banner.png");

		texMap.put("chain_segment", "chain/chain_segment.png");
		texMap.put("effect_square", "chain/effect_square.png");

		texMap.put("effect_destroy", "effect/destroy.png");
		texMap.put("effect_draw_card", "effect/draw_card.png");
		texMap.put("effect_gather_items", "effect/gather_items.png");
		texMap.put("effect_melee_damage", "effect/melee_damage.png");
		texMap.put("effect_move", "effect/move.png");
		texMap.put("effect_teleport", "effect/teleport.png");
		texMap.put("effect_ranged_damage", "effect/ranged_damage.png");
		texMap.put("effect_regenesis", "effect/regenesis.png");
		texMap.put("effect_restore_health", "effect/restore_health.png");
		texMap.put("effect_spawn_structure", "effect/spawn_structure.png");
		texMap.put("effect_interact", "effect/interact.png");
		texMap.put("effect_build_deck", "effect/build_deck.png");

		texMap.put("card_art_meteor", "card_art/meteor.png");
		texMap.put("card_art_extra_preparation", "card_art/extra_preparation.png");
		texMap.put("card_art_bash", "card_art/bash.png");
		texMap.put("card_art_refreshing_break", "card_art/refreshing_break.png");
		texMap.put("card_art_zap", "card_art/zap.png");
		texMap.put("card_art_move", "card_art/move.png");
		texMap.put("card_art_gather", "card_art/gather.png");
		texMap.put("card_art_cut_tree", "card_art/cut_tree.png");
		texMap.put("card_art_teleport", "card_art/teleport.png");
		texMap.put("card_art_house", "card_art/build_house.png");
		texMap.put("card_art_regenesis", "card_art/regenesis.png");
		texMap.put("card_art_overclocked_machinery", "card_art/overclocked_machinery.png");
		texMap.put("card_art_planning_table", "card_art/overclocked_machinery.png"); // TODO
		texMap.put("card_art_interact", "card_art/move.png"); // TODO

		texMap.put("health", "actor/health.png");
		texMap.put("actor_nomad", "actor/nomad.png");
		texMap.put("actor_wolf", "actor/wolf.png");
		texMap.put("actor_jary", "actor/jary.png");
		texMap.put("actor_aura", "actor/aura.png");

		texMap.put("npc_village_farmer", "actor/farmer.png");
		texMap.put("npc_goat", "actor/goat.png");

		texMap.put("resource_tree", "actor/tree.png");
		texMap.put("house_full", "actor/house_full.png");
		texMap.put("overclocked_machinery", "card_art/overclocked_machinery.png"); // TODO
		texMap.put("planning_table", "actor/planning_table.png");

		texMap.put("logo_large", "logo/logo_large.png");
		texMap.put("logo_small", "logo/logo_small.png");
		texMap.put("logo_placeholder", "logo/logo_placeholder.png");

		texMap.put("particle_circle", "particles/circle.png");
		texMap.put("particle_hexagon", "particles/hexagon.png");
		texMap.put("particle_card", "particles/card.png");

		texMap.put("item_meat", "item/meat.png");
		texMap.put("item_wood", "item/wood.png");
		texMap.put("item_stone", "item/stone.png");
		Map<String, Future<Texture>> fTexMap = new HashMap<>();
		texMap.forEach((name, path) -> fTexMap.put(name, loader().submit(new NomadsTextureLoadTask(path))));

		try {
			ElementBufferObject ebo = febo.get();
			VertexBufferObject vbo = fvbo.get();
			Future<VertexArrayObject> fvao = loader().submit(new VertexArrayObjectLoadTask(ebo, vbo));
			rp.putVAO("hexagon", fvao.get());

			int w = 1000;
			int h = 800;
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

			VertexArrayObject hexagonVAO = rp.getVAO("hexagon");
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

			EllipseShaderProgram ellipseSP = new EllipseShaderProgram(transformationVS, fEllipseFS.get());
			loader().submit(new ShaderProgramLoadTask(ellipseSP)).get();
			EllipseRenderer ellipseRenderer = new EllipseRenderer(ellipseSP, rectangleVAO);
			rp.putRenderer("ellipse", ellipseRenderer);

			rp.putRenderer("rectangle", new RectangleRenderer(rp.defaultShaderProgram(), rectangleVAO));

			rp.putRenderer("rootGui", new RootGuiRenderer());

			fTexMap.forEach((name, fTexture) -> {
				try {
					rp.putTexture(name, fTexture.get());
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			});

			NomadRealmsModelLoadTask modelLoadTask = new NomadRealmsModelLoadTask("3d/tile.obj", new ResourcePack(glContext()));
			Model model = loader().submit(modelLoadTask).get();

			rp.putRenderer("chainHeap", new ChainHeapRenderer(rp));

			LoadingGameData data = (LoadingGameData) context().data();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render() {
	}

}
