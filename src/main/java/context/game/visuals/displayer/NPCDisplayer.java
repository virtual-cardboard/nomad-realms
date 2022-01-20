package context.game.visuals.displayer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayable.TextureBodyPart;
import context.visuals.lwjgl.Texture;
import model.actor.NPC;
import model.state.GameState;

public class NPCDisplayer extends CardPlayerDisplayer<NPC> {

	private long npcID;
	private Vector2f lastDirection = new Vector2f(0, 1);

	public NPCDisplayer(long npcID) {
		this.npcID = npcID;
	}

	@Override
	protected void init(ResourcePack resourcePack) {
		super.init(resourcePack);
		Texture texture = resourcePack.getTexture("tiny_toad");
		TextureBodyPart actorBodyPart = new TextureBodyPart(texture, 0.5f);
		addBodyPart(actorBodyPart);
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		NPC npc = (NPC) state.cardPlayer(npcID);
//		lastDirection = lastDirection.add(npc.direction().scale(0.2f)).normalise();
		displayBodyParts(glContext, s, state, camera, npc, alpha, lastDirection);
		displayHealth(glContext, s, npc, state, camera);
		displayQueue(glContext, s, npc, state, camera);
		displayEffectChains(glContext, s, npc, state, camera);
	}

}
