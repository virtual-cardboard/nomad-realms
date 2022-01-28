package context.game.visuals.displayer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayable.TextureBodyPart;
import context.visuals.lwjgl.Texture;
import model.actor.resource.TreeActor;
import model.state.GameState;

public class TreeActorDisplayer extends ActorDisplayer<TreeActor> {

	private long treeID;

	public TreeActorDisplayer(long treeID) {
		this.treeID = treeID;
	}

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		Texture texture = resourcePack.getTexture("resource_tree");
		TextureBodyPart actorBodyPart = new TextureBodyPart(texture, 0.8f);
		addBodyPart(actorBodyPart);
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		displayBodyParts(glContext, s, state, camera, (TreeActor) state.actor(treeID), alpha, new Vector2f(0, 1));
	}

}
