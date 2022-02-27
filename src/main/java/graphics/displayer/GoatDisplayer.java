package graphics.displayer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import model.actor.npc.animal.Goat;
import model.state.GameState;

public class GoatDisplayer extends CardPlayerDisplayer<Goat> {

	private Texture goatTex;

	public GoatDisplayer(long actorID) {
		super(actorID);
	}

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		goatTex = resourcePack.getTexture("npc_goat");
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		Goat goat = (Goat) state.actor(actorID());
		Vector2f screenPos = goat.screenPos(camera, s);
		float ws = s.worldScale;
		textureRenderer.render(goatTex, screenPos.x - ws * 0.9f, screenPos.y - ws * 0.8f, ws * 1.4f, ws * 1.1f);
		displayHealth(glContext, s, goat, state, camera);
		displayQueue(glContext, s, goat, state, camera);
	}

}