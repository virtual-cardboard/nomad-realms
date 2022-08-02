package graphics.displayer;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;
import model.actor.resource.TreeActor;
import model.state.GameState;

public class TreeActorDisplayer extends ActorDisplayer<TreeActor> {

	private Texture treeTexture;

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		treeTexture = resourcePack.getTexture("resource_tree");
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		TreeActor tree = (TreeActor) actorId().getFrom(state);
		Vector2f sp = tree.screenPos(camera, s);
		Vector2f dim = treeTexture.dimensions().scale(0.4f);
		textureRenderer.render(treeTexture, sp.x() - dim.x() * 0.5f,
				sp.y() - dim.y() * 0.5f + s.tileHeight() * 0.2f, dim.x(), dim.y());
	}

}
