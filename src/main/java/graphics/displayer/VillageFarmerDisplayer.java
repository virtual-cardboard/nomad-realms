package graphics.displayer;

import static context.visuals.colour.Colour.rgba;

import java.util.Set;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import engine.common.math.Vector2f;
import graphics.displayable.TextureBodyPart;
import model.actor.npc.village.farmer.VillageFarmer;
import model.item.Item;
import model.item.ItemCollection;
import model.state.GameState;

public class VillageFarmerDisplayer extends CardPlayerDisplayer<VillageFarmer> {

	private Vector2f lastDirection = new Vector2f(0, 1);

	public VillageFarmerDisplayer(long farmerID) {
		super(farmerID);
	}

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);

		Texture texture = resourcePack.getTexture("npc_village_farmer");
		TextureBodyPart actorBodyPart = new TextureBodyPart(texture, 0.4f);
		actorBodyPart.height = 20;
		addBodyPart(actorBodyPart);
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		VillageFarmer farmer = (VillageFarmer) state.actor(actorID());
		displayBodyParts(glContext, s, state, camera, farmer, alpha, lastDirection);
		displayHealth(glContext, s, farmer, state, camera);
		displayQueue(glContext, s, farmer, state, camera);
		displayEffectChains(glContext, s, farmer, state, camera);

		Vector2f sp = farmer.screenPos(camera, s);

		rectangleRenderer().render(sp.x - 60, sp.y - 150, 120, 50, rgba(199, 182, 121, 120));
		rectangleRenderer().render(sp.x - 56, sp.y - 146, 112, 42, rgba(245, 224, 147, 120));
		ItemCollection inventory = farmer.inventory();
		Set<Item> keySet = inventory.keySet();
		int i = 0;
		for (Item item : keySet) {
			float itemX = sp.x - 50 + i * 50;
			float itemY = sp.y - 145;

			Texture texture = resourcePack().getTexture("item_" + item.toString().toLowerCase());
			textureRenderer.render(texture, itemX, itemY, 40, 40);
			textRenderer.alignCenterHorizontal();
			textRenderer.render(itemX + 30, itemY + 30, inventory.get(item) + "", 0, font, 20, 255);
			i++;
		}
	}

}
