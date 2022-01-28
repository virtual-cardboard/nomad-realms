package context.game.visuals.displayer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.game.visuals.displayable.TextureBodyPart;
import context.visuals.lwjgl.Texture;
import model.actor.npc.VillageFarmer;
import model.state.GameState;

public class VillageFarmerDisplayer extends CardPlayerDisplayer<VillageFarmer> {

	private long farmerID;
	private Vector2f lastDirection = new Vector2f(0, 1);

	public VillageFarmerDisplayer(long farmerID) {
		this.farmerID = farmerID;
	}

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		Texture texture = resourcePack.getTexture("npc_village_farmer");
		TextureBodyPart actorBodyPart = new TextureBodyPart(texture, 0.6f);
		addBodyPart(actorBodyPart);
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		VillageFarmer farmer = (VillageFarmer) state.actor(farmerID);
		displayBodyParts(glContext, s, state, camera, farmer, alpha, lastDirection);
		displayHealth(glContext, s, farmer, state, camera);
		displayQueue(glContext, s, farmer, state, camera);
		displayEffectChains(glContext, s, farmer, state, camera);
	}

}
