package graphics.displayer;

import static java.lang.Math.PI;
import static java.lang.Math.sin;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;
import math.WorldPos;
import model.actor.Nomad;
import model.state.GameState;

public class NomadDisplayer extends CardPlayerDisplayer<Nomad> {

	private static final int NUM_HOP_FRAMES = 4;

	private WorldPos prevPosition = null;
	private int hopFrame = 0;
	private Texture nomadTexture;

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		nomadTexture = resourcePack.getTexture("actor_jary");
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float dt) {
		Nomad nomad = (Nomad) actorId().getFrom(state);
//		lastDirection = lastDirection.add(nomad.direction().scale(0.2f)).normalise();
//		displayHealth(glContext, s, nomad, state, camera);
//		displayQueue(glContext, s, nomad, state, camera);
		displayEffectChains(glContext, s, nomad, state, camera);
//		displayBodyParts(glContext, s, state, camera, nomad, alpha, lastDirection);
		Vector2f sp = nomad.screenPos(camera, s);

		if (prevPosition != null && !prevPosition.equals(nomad.worldPos())) {
			hopFrame++;
			Vector2f prevSp = prevPosition.screenPos(camera, s);
			sp = prevSp.add(sp.sub(prevSp).scale(1f * hopFrame / NUM_HOP_FRAMES)).add(0, (float) (-sin(PI * hopFrame / NUM_HOP_FRAMES) * 40));
			if (hopFrame == NUM_HOP_FRAMES) {
				hopFrame = 0;
				prevPosition = nomad.worldPos();
			}
			// DON'T set prevPosition
		} else {
			hopFrame = 0;
			prevPosition = nomad.worldPos();
		}

		Vector2f dim = nomadTexture.dimensions().scale(s.actorScale);
		textureRenderer.render(nomadTexture, sp.x() - dim.x() / 2, sp.y() - dim.y() + s.worldScale * 0.2f,
				dim.x(), dim.y());

//		float x = screenPos.x - 60;
//		float y = screenPos.y - 120;
//		textRenderer.alignCenter();
//		textRenderer.render(x, y, "P" + longID, 120, font, 30, Colour.rgb(69, 165, 255));
	}

}
