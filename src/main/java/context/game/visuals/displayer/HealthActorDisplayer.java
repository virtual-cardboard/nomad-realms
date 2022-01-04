package context.game.visuals.displayer;

import static context.visuals.colour.Colour.rgb;

import app.NomadsSettings;
import common.math.Matrix4f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.text.GameFont;
import model.actor.HealthActor;
import model.state.GameState;

public abstract class HealthActorDisplayer<T extends HealthActor> extends ActorDisplayer<T> {

	protected TextRenderer textRenderer;
	protected GameFont font;
	private Texture health;

	@Override
	protected void init(ResourcePack resourcePack) {
		super.init(resourcePack);
		textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		health = resourcePack.getTexture("health");
		font = resourcePack.getFont("langar");
	}

	protected final void displayHealth(GLContext glContext, NomadsSettings s, T t, GameState state, GameCamera camera) {
		float x = t.screenPos(camera, s).x;
		float y = t.screenPos(camera, s).y;
		textureRenderer.render(health, x - 35, y - 65, 1);
		textRenderer.render(new Matrix4f().translate(x - 52, y - 80), "" + t.health(), 0, font, 30, rgb(255, 255, 255));
	}

}
