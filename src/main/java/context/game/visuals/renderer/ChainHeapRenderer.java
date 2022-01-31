package context.game.visuals.renderer;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.GameRenderer;
import context.visuals.renderer.TextureRenderer;
import model.card.chain.ChainEvent;
import model.chain.ChainHeap;
import model.chain.EffectChain;

public class ChainHeapRenderer extends GameRenderer {

	private TextureRenderer textureRenderer;
	private Texture chainSegment;
	private Texture effectSquare;

	public ChainHeapRenderer(GLContext glContext, ResourcePack rp) {
		super(glContext);
		textureRenderer = rp.getRenderer("texture", TextureRenderer.class);
		chainSegment = rp.getTexture("chain_segment");
		effectSquare = rp.getTexture("effect_square");
	}

	public void render(ChainHeap chainHeap, NomadsSettings s) {
		for (EffectChain chain : chainHeap) {
			for (int i = 0; i < chain.size(); i++) {
//				ChainEvent event = chain.get(i);

			}
		}
	}

	private Vector2f effectSquareLocation(ChainEvent event, NomadsSettings s) {
//		event.playerID()
		return null;
	}

}
