package graphics.displayer;

import static context.visuals.colour.Colour.rgb;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.text.GameFont;
import model.actor.health.HealthActor;
import model.state.GameState;

public abstract class HealthActorDisplayer<T extends HealthActor> extends ActorDisplayer<T> {

	protected TextRenderer textRenderer;
	protected GameFont font;
	private Texture health;

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		health = resourcePack.getTexture("health");
		font = resourcePack.getFont("langar");
	}

	protected final void displayHealth(GLContext glContext, NomadsSettings s, T t, GameState state, GameCamera camera) {
		float x = t.screenPos(camera, s).x();
		float y = t.screenPos(camera, s).y();
		textureRenderer.render(health, x, y - 85, 1);
		textRenderer.alignCenterHorizontal();
		textRenderer.render(x - 100, y - 66, "" + t.health(), 200, font, 30, rgb(255, 255, 255));
	}

}
