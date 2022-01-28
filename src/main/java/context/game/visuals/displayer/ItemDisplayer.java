package context.game.visuals.displayer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.EllipseRenderer;
import context.visuals.renderer.TextureRenderer;
import model.actor.ItemActor;
import model.state.GameState;

public class ItemDisplayer extends ActorDisplayer<ItemActor> {

	private EllipseRenderer ellipseRenderer;

	private long itemActorID;
	private Texture itemTexture;

	public ItemDisplayer(long itemActorID) {
		this.itemActorID = itemActorID;
	}

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		this.textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		ellipseRenderer = resourcePack.getRenderer("ellipse", EllipseRenderer.class);
		itemTexture = resourcePack.getTexture("item_" + state.item(itemActorID).item().toString().toLowerCase());
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float alpha) {
		ItemActor item = state.item(itemActorID);
		Vector2f screenPos = item.screenPos(camera, s);
//		textureRenderer.render(itemTexture, screenPos.x, screenPos.y + (float) sin((System.currentTimeMillis() - 1643389693000L) * 0.005f) * 10, 1);
		textureRenderer.render(itemTexture, screenPos.x - 20, screenPos.y - 20, 40, 40);
//		ellipseRenderer.renderPixelCoords(800, 100, 100, 100, rgba(0, 0, 0, 255));
	}

}
