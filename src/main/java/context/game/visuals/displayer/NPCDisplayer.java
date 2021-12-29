package context.game.visuals.displayer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayable.TextureBodyPart;
import context.visuals.lwjgl.Texture;
import model.GameState;
import model.actor.NPC;

public class NPCDisplayer extends CardPlayerDisplayer<NPC> {

	private NPC npc;
	private Vector2f lastDirection = new Vector2f(0, 1);

	public NPCDisplayer(NPC npc) {
		this.npc = npc;
	}

	@Override
	public void init(ResourcePack resourcePack) {
		Texture texture = resourcePack.getTexture("tiny_toad");
		TextureBodyPart actorBodyPart = new TextureBodyPart(texture, 0.5f);
		addBodyPart(actorBodyPart);
		super.init(resourcePack);
	}

	@Override
	public void display(GLContext glContext, Vector2f screenDim, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		lastDirection = lastDirection.add(npc.direction().scale(0.2f)).normalise();
		displayBodyParts(glContext, screenDim, s, state, camera, npc, alpha, lastDirection);
		displayHealth(glContext, screenDim, s, npc, state, camera);
		displayQueue(glContext, screenDim, s, npc, state, camera);
		displayEffectChains(glContext, screenDim, s, npc, state, camera);
	}

}
