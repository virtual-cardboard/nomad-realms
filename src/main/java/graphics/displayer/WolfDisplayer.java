package graphics.displayer;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;
import model.actor.npc.animal.Wolf;
import model.state.GameState;

public class WolfDisplayer extends CardPlayerDisplayer<Wolf> {

	private Texture wolfTex;

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		wolfTex = resourcePack.getTexture("actor_wolf");
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float dt) {
		Wolf wolf = (Wolf) actorId().getFrom(state);
		Vector2f sp = wolf.screenPos(camera, s);
		Vector2f dim = wolfTex.dimensions().scale(s.actorScale);
		textureRenderer.render(wolfTex, sp.x() - dim.x() / 2, sp.y() - dim.y() + s.worldScale * 0.2f,
				dim.x(), dim.y());
		displayHealth(glContext, s, wolf, state, camera);
//		displayQueue(glContext, s, wolf, state, camera);
	}

}
