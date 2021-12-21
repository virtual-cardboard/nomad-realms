package context.game.visuals.displayer;

import static context.visuals.colour.Colour.rgb;
import static context.visuals.colour.Colour.rgba;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import event.game.logicprocessing.CardPlayedEvent;
import model.actor.Nomad;
import model.card.CardQueue;

public class NomadDisplayer extends CardPlayerDisplayer<Nomad> {

	private ResourcePack resourcePack;

	private Nomad nomad;
	private TextureRenderer textureRenderer;
	private TextRenderer textRenderer;
	private Texture nomadBody;
	private Texture nomadShirt;
	private Texture nomadCape;
	private Texture nomadEyes;
	private Texture health;
	private GameFont font;
	private RectangleRenderer rectangleRenderer;

	public NomadDisplayer(Nomad nomad) {
		this.nomad = nomad;
	}

	@Override
	public void init(ResourcePack resourcePack) {
		this.resourcePack = resourcePack;
		nomadBody = resourcePack.getTexture("nomad_body");
		nomadShirt = resourcePack.getTexture("nomad_shirt");
		nomadCape = resourcePack.getTexture("nomad_cape");
		nomadEyes = resourcePack.getTexture("nomad_eyes");
		health = resourcePack.getTexture("health");
		textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		rectangleRenderer = resourcePack.getRenderer("rectangle", RectangleRenderer.class);
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		font = resourcePack.getFont("langar");
	}

	@Override
	public void display(GLContext glContext, Vector2f screenDim, GameCamera camera, CardQueue queue) {
		Vector2f pos = nomad.screenPos(camera);
		float x = pos.x;
		float y = pos.y;
		textureRenderer.render(glContext, screenDim, nomadBody, x, y, 1);
		textureRenderer.render(glContext, screenDim, nomadShirt, x, y + 12, 1);
		textureRenderer.render(glContext, screenDim, nomadCape, x - 9, y + 15, 1);
		textureRenderer.render(glContext, screenDim, nomadEyes, x + 1, y - 25, 1);
		textureRenderer.render(glContext, screenDim, health, x - 35, y - 65, 1);
		rectangleRenderer.render(glContext, screenDim, x + 10, y - 90, 120, 35, rgba(186, 157, 93, 230));
		textRenderer.render(glContext, screenDim, new Matrix4f().translate(x - 52, y - 80), "" + nomad.health(), 0, font, 30, rgb(255, 255, 255));
		for (int i = 0; i < queue.size(); i++) {
			CardPlayedEvent cpe = queue.get(i);
			Texture cardTex = resourcePack.getTexture(cpe.card().name().replace(' ', '_').toLowerCase());
			textureRenderer.render(glContext, screenDim, cardTex, x + 36 + i * 40, y - 40, 0.4f);
		}
	}

}
