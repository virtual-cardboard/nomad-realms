package context.game.visuals.renderer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.TextureRenderer;
import model.card.chain.ChainEvent;
import model.chain.ChainHeap;
import model.chain.EffectChain;
import model.state.GameState;

public class ChainHeapRenderer extends GameRenderer {

	private ResourcePack rp;

	private TextureRenderer textureRenderer;
	private Texture chainSegment;
	private Texture effectSquare;

	public ChainHeapRenderer(ResourcePack rp) {
		this.rp = rp;
		textureRenderer = rp.getRenderer("texture", TextureRenderer.class);
		chainSegment = rp.getTexture("chain_segment");
		effectSquare = rp.getTexture("effect_square");
	}

	public void render(ChainHeap chainHeap, GameState state, GameCamera camera, NomadsSettings s) {
		for (EffectChain chain : chainHeap) {
			for (int i = 0; i < chain.size(); i++) {
				ChainEvent event = chain.get(i);
				if (!event.shouldDisplay()) {
					continue;
				}
				Vector2f squareLoc = state.actor(event.playerID()).screenPos(camera, s).add(0, -150);
				Texture effectTexture = rp.getTexture("effect_" + event.textureName());
				float length = 50 * s.guiScale;
				float halfLength = length / 2;
				textureRenderer.render(effectSquare, squareLoc.x - halfLength, squareLoc.y - halfLength, length, length);
				textureRenderer.render(effectTexture, squareLoc.x - halfLength, squareLoc.y - halfLength, length, length);
			}
		}
	}

}
