package context.game.visuals.displayer;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.colour.Colour;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import model.actor.Nomad;

public class NomadDisplayer extends ActorDisplayer<Nomad> {

	private Nomad nomad;
	private TextureRenderer textureRenderer;
	private TextRenderer textRenderer;
	private Texture nomadBody;
	private Texture nomadShirt;
	private Texture nomadCape;
	private Texture nomadEyes;
	private Texture health;
	private GameFont font;

	public NomadDisplayer(Nomad nomad, ResourcePack resourcePack, TextureRenderer textureRenderer) {
		this.nomad = nomad;
		nomadBody = resourcePack.getTexture("nomad_body");
		nomadShirt = resourcePack.getTexture("nomad_shirt");
		nomadCape = resourcePack.getTexture("nomad_cape");
		nomadEyes = resourcePack.getTexture("nomad_eyes");
		health = resourcePack.getTexture("health");
		textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		font = resourcePack.getFont("baloo2");
		this.textureRenderer = textureRenderer;
	}

	@Override
	public void display(GLContext glContext, Vector2f rootGuiDimensions, GameCamera camera) {
		Vector2f pos = nomad.pos().copy().sub(camera.pos());
		float cx = pos.x;
		float cy = pos.y;
		textureRenderer.render(glContext, rootGuiDimensions, nomadBody, cx, cy, 1);
		textureRenderer.render(glContext, rootGuiDimensions, nomadShirt, cx, cy + 12, 1);
		textureRenderer.render(glContext, rootGuiDimensions, nomadCape, cx - 9, cy + 15, 1);
		textureRenderer.render(glContext, rootGuiDimensions, nomadEyes, cx + 1, cy - 25, 1);
		textureRenderer.render(glContext, rootGuiDimensions, health, cx - 5, cy - 65, 1);
		textRenderer.render(glContext, rootGuiDimensions, new Matrix4f().translate(cx - 16, cy - 80), "" + nomad.health(), -1, font, 40,
				Colour.rgb(255, 255, 255));
	}

}
