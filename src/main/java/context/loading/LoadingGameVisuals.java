package context.loading;

import static context.game.visuals.shape.HexagonVertexArrayObject.createHexagonEBOLoadTask;
import static context.game.visuals.shape.HexagonVertexArrayObject.createHexagonVBOLoadTask;
import static context.visuals.lwjgl.ShaderType.FRAGMENT;
import static org.lwjgl.opengl.GL11.GL_ALWAYS;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.GL_DEPTH24_STENCIL8;
import static org.lwjgl.opengl.GL30.GL_DEPTH_STENCIL_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import common.loader.loadtask.*;
import context.game.visuals.renderer.hexagon.HexagonShaderProgram;
import context.game.visuals.shape.HexagonVertexArrayObject;
import context.visuals.GameVisuals;
import context.visuals.builtin.*;
import context.visuals.lwjgl.*;
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

		TransformationVertexShader transformationVS = resourcePack().transformationVertexShader();
		TexturedTransformationVertexShader texturedTransformationVS = resourcePack().texturedTransformationVertexShader();

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
		texMap.put("card_front", "card_template/card_front.png");
		texMap.put("card_banner", "card_template/card_banner.png");
		texMap.put("card_logo", "card_template/card_logo.png");

		texMap.put("meteor", "card_art/meteor.png");
		texMap.put("extra_preparation", "card_art/extra_preparation.png");
		texMap.put("zap", "card_art/zap.png");
		texMap.put("teleport", "card_art/teleport.png");
		texMap.put("regenesis", "card_art/regenesis.png");

		texMap.put("health", "actor/health.png");
		texMap.put("nomad_body", "actor/nomad_body.png");
		texMap.put("nomad_shirt", "actor/nomad_shirt.png");
		texMap.put("nomad_cape", "actor/nomad_cape.png");
		texMap.put("nomad_eyes", "actor/nomad_eyes.png");

		texMap.put("particle", "particles/particle.png");
		Map<String, Future<Texture>> fTexMap = new HashMap<>();
		texMap.forEach((name, path) -> fTexMap.put(name, loader().submit(new NomadsTextureLoadTask(path))));

		try {
			ElementBufferObject ebo = febo.get();
			VertexBufferObject vbo = fvbo.get();
			Future<VertexArrayObject> fvao = loader().submit(new VertexArrayObjectLoadTask(new HexagonVertexArrayObject(), ebo, vbo));
			resourcePack().putVAO("hexagon", fvao.get());

			int w = rootGui().width();
			int h = rootGui().height();
			FrameBufferObject fbo = loader().submit(new FrameBufferObjectLoadTask()).get();
			Texture emptyTexture = loader().submit(new EmptyTextureLoadTask(w, h)).get();
			RenderBufferObject rbo = loader().submit(new RenderBufferObjectLoadTask(GL_DEPTH24_STENCIL8, GL_DEPTH_STENCIL_ATTACHMENT, w, h)).get();
			fbo.bind(glContext());
			fbo.attachTexture(emptyTexture);
			fbo.attachRenderBufferObject(rbo);
			FrameBufferObject.unbind(glContext());
			if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
				System.out.println("AAAHAHAHHHHHAGGGGGGGh NOOOOO");
			} else {
				System.out.println("yay");
			}
			resourcePack().putFBO("render", fbo);
			resourcePack().putTexture("render", emptyTexture);

			Texture baloo2Tex = fBaloo2Tex.get();
			Future<GameFont> fBaloo2Font = loader().submit(new NomadRealmsFontLoadTask("fonts/baloo2.vcfont", baloo2Tex));
			GameFont baloo2Font = fBaloo2Font.get();
			resourcePack().putFont("baloo2", baloo2Font);

			Texture langarTex = fLangarTex.get();
			Future<GameFont> fLangarFont = loader().submit(new NomadRealmsFontLoadTask("fonts/langar.vcfont", langarTex));
			GameFont langarFont = fLangarFont.get();
			resourcePack().putFont("langar", langarFont);

			Shader hexagonFS = fHexagonFS.get();
			HexagonShaderProgram hexagonSP = new HexagonShaderProgram(transformationVS, hexagonFS);
			loader().submit(new ShaderProgramLoadTask(hexagonSP)).get();
			resourcePack().putShaderProgram("hexagon", hexagonSP);

			Shader textFS = fTextFS.get();
			TextShaderProgram textSP = new TextShaderProgram(texturedTransformationVS, textFS);
			loader().submit(new ShaderProgramLoadTask(textSP)).get();
			resourcePack().putShaderProgram("text", textSP);

			Shader textureFS = fTextureFS.get();
			TextureShaderProgram textureSP = new TextureShaderProgram(texturedTransformationVS, textureFS);
			loader().submit(new ShaderProgramLoadTask(textureSP)).get();
			resourcePack().putShaderProgram("texture", textureSP);

			Shader lineFS = fLineFS.get();
			LineShaderProgram lineSP = new LineShaderProgram(transformationVS, lineFS);
			loader().submit(new ShaderProgramLoadTask(lineSP)).get();
			resourcePack().putShaderProgram("line", lineSP);

			resourcePack().putRenderer("rectangle", new RectangleRenderer(resourcePack().defaultShaderProgram(), resourcePack().rectangleVAO()));

			fTexMap.forEach((name, fTexture) -> {
				try {
					resourcePack().putTexture(name, fTexture.get());
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
