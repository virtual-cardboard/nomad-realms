package context.game.visuals.renderer;

import static context.visuals.colour.Colour.rgb;

import app.NomadsSettings;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.LineRenderer;
import context.visuals.renderer.TextureRenderer;
import engine.common.math.Vector2f;
import model.actor.health.cardplayer.CardPlayer;
import model.chain.ChainHeap;
import model.chain.EffectChain;
import model.chain.event.ChainEvent;
import model.state.GameState;

public class ChainHeapRenderer extends GameRenderer {

	private static final float SPACING = 50;

	private ResourcePack rp;

	private TextureRenderer textureRenderer;
	private LineRenderer lineRenderer;

	private Texture chainSegment;
	private Texture effectSquare;

	public ChainHeapRenderer(ResourcePack rp) {
		this.rp = rp;
		textureRenderer = rp.getRenderer("texture", TextureRenderer.class);
		lineRenderer = rp.getRenderer("line", LineRenderer.class);
		chainSegment = rp.getTexture("chain_segment");
		effectSquare = rp.getTexture("effect_square");
	}

	public void render(ChainHeap chainHeap, GameState state, GameCamera camera, NomadsSettings s) {
		for (EffectChain chain : chainHeap) {
			Vector2f lastPos = null;
			float chainXOffset = -(chain.size() - 1) * 0.5f * SPACING;
			for (int i = 0; i < chain.size(); i++) {
				ChainEvent event = chain.get(i);
				if (!event.shouldDisplay()) {
					continue;
				}
				CardPlayer player = event.playerID().getFrom(state);
				if (player == null) {
					// The player is most likely dead, but the event they created is still there
					// We simply don't draw the event
					return;
				}
				Vector2f pos = player.screenPos(camera, s).add(chainXOffset + i * SPACING, -150);
				Texture effectTexture = rp.getTexture("effect_" + event.textureName());
				float length = 50 * s.guiScale;
				float halfLength = length / 2;

				textureRenderer.setDiffuse(0xFFFFFFB9);

				if (lastPos != null) {
					lineRenderer.render(lastPos.x(), lastPos.y(), pos.x(), pos.y(), 6, rgb(247, 25, 210));
				}

				textureRenderer.render(effectSquare, pos.x() - halfLength, pos.y() - halfLength, length, length);
				textureRenderer.render(effectTexture, pos.x() - halfLength, pos.y() - halfLength, length, length);

				textureRenderer.resetDiffuse();
				lastPos = pos;
			}
		}
	}

}
