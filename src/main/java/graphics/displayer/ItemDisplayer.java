package graphics.displayer;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.EllipseRenderer;
import context.visuals.renderer.TextureRenderer;
import engine.common.math.Vector2f;
import model.actor.ItemActor;
import model.state.GameState;

public class ItemDisplayer extends ActorDisplayer<ItemActor> {

	private EllipseRenderer ellipseRenderer;

	private Texture itemTexture;

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		this.textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		ellipseRenderer = resourcePack.getRenderer("ellipse", EllipseRenderer.class);
		itemTexture = resourcePack.getTexture("item_" + ((ItemActor) actorId().getFrom(state)).item().toString().toLowerCase());
	}

	@Override
	public void display(GLContext glContext, NomadsSettings s, GameState state, GameCamera camera, float dt) {
		ItemActor item = (ItemActor) actorId().getFrom(state);
		Vector2f pos = item.screenPos(camera, s);
//		textureRenderer.render(itemTexture, screenPos.x, screenPos.y + (float) sin((System.currentTimeMillis() - 1643389693000L) * 0.005f) * 10, 1);
		textureRenderer.render(itemTexture, pos.x() - s.tileWidth() * 0.35f, pos.y() - s.tileHeight() * 0.5f, s.worldScale * 0.9f,
				s.worldScale * 0.9f * itemTexture.height() / itemTexture.width());
//		ellipseRenderer.renderPixelCoords(800, 100, 100, 100, rgba(0, 0, 0, 255));
	}

}
