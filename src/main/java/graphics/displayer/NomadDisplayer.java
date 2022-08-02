package graphics.displayer;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;
import model.actor.Nomad;
import model.state.GameState;

public class NomadDisplayer extends CardPlayerDisplayer<Nomad> {

	private Vector2f lastDirection = new Vector2f(0, 1);
	private Texture nomadTexture;

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		nomadTexture = resourcePack.getTexture("actor_jary");
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		Nomad nomad = (Nomad) actorId().getFrom(state);
//		lastDirection = lastDirection.add(nomad.direction().scale(0.2f)).normalise();
//		displayHealth(glContext, s, nomad, state, camera);
//		displayQueue(glContext, s, nomad, state, camera);
		displayEffectChains(glContext, s, nomad, state, camera);
//		displayBodyParts(glContext, s, state, camera, nomad, alpha, lastDirection);
		Vector2f sp = nomad.screenPos(camera, s);
		float side = s.worldScale * 1.1f;
		textureRenderer.render(nomadTexture, sp.x() - side / 2, sp.y() - side * 0.8f, side, side);
//		float x = screenPos.x - 60;
//		float y = screenPos.y - 120;
//		textRenderer.alignCenter();
//		textRenderer.render(x, y, "P" + longID, 120, font, 30, Colour.rgb(69, 165, 255));
	}

}
