package context.game.visuals.displayer;

import static context.visuals.colour.Colour.rgb;
import static context.visuals.colour.Colour.rgba;

import java.util.List;
import java.util.stream.Collectors;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.lwjgl.Texture;
import context.visuals.renderer.TextRenderer;
import context.visuals.renderer.TextureRenderer;
import context.visuals.text.GameFont;
import event.game.logicprocessing.CardPlayedEvent;
import model.GameState;
import model.actor.CardPlayer;
import model.card.CardQueue;
import model.chain.ChainEvent;
import model.chain.EffectChain;

public abstract class CardPlayerDisplayer<T extends CardPlayer> {

	protected ResourcePack resourcePack;

	protected TextureRenderer textureRenderer;
	protected TextRenderer textRenderer;
	protected RectangleRenderer rectangleRenderer;
	private Texture health;
	protected GameFont font;

	private Texture chainSegment;
	private Texture effectSquare;

	public void init(ResourcePack resourcePack) {
		this.resourcePack = resourcePack;
		textureRenderer = resourcePack.getRenderer("texture", TextureRenderer.class);
		textRenderer = resourcePack.getRenderer("text", TextRenderer.class);
		rectangleRenderer = resourcePack.getRenderer("rectangle", RectangleRenderer.class);
		health = resourcePack.getTexture("health");
		chainSegment = resourcePack.getTexture("chain_segment");
		effectSquare = resourcePack.getTexture("effect_square");
		font = resourcePack.getFont("langar");
	}

	public abstract void display(GLContext glContext, Vector2f screenDim, GameState state, GameCamera camera);

	protected final void displayHealth(GLContext glContext, Vector2f screenDim, CardPlayer cardPlayer, GameState state, GameCamera camera) {
		float x = cardPlayer.screenPos(camera).x;
		float y = cardPlayer.screenPos(camera).y;
		textureRenderer.render(glContext, screenDim, health, x - 35, y - 65, 1);
		textRenderer.render(glContext, screenDim, new Matrix4f().translate(x - 52, y - 80), "" + cardPlayer.health(), 0, font, 30, rgb(255, 255, 255));
	}

	protected final void displayQueue(GLContext glContext, Vector2f screenDim, CardPlayer cardPlayer, GameState state, GameCamera camera) {
		float x = cardPlayer.screenPos(camera).x;
		float y = cardPlayer.screenPos(camera).y;
		rectangleRenderer.render(glContext, screenDim, x + 10, y - 90, 120, 35, rgba(186, 157, 93, 230));
		CardQueue queue = cardPlayer.cardDashboard().queue();
		for (int i = 0; i < queue.size(); i++) {
			CardPlayedEvent cpe = queue.get(i);
			Texture cardTex = resourcePack.getTexture(cpe.card().name().replace(' ', '_').toLowerCase());
			textureRenderer.render(glContext, screenDim, cardTex, x + 36 + i * 40, y - 40, 0.4f);
		}
	}

	protected final void displayEffectChains(GLContext glContext, Vector2f screenDim, CardPlayer cardPlayer, GameState state, GameCamera camera) {
		float x = cardPlayer.screenPos(camera).x;
		float y = cardPlayer.screenPos(camera).y;
		List<EffectChain> chains = cardPlayer.chains();
		for (int i = 0; i < chains.size(); i++) {
			EffectChain chain = chains.get(i);
			List<ChainEvent> toDisplay = chain.stream().filter(ChainEvent::shouldDisplay).collect(Collectors.toList());

			float chainX = x - (toDisplay.size() - 1) * 0.5f * 40;
			float chainY = y - 100 - i * (effectSquare.height() * 0.3f + 5);
			for (int j = 0; j < toDisplay.size(); j++) {
//				ChainEvent event = toDisplay.get(j);
				textureRenderer.render(glContext, screenDim, effectSquare, chainX + j * 50, chainY, 0.3f);
				if (j != toDisplay.size() - 1) {
					textureRenderer.render(glContext, screenDim, chainSegment,
							chainX - effectSquare.width() * 0.15f - chainSegment.width() * 0.15f + j * 50, chainY, 0.3f);
				}
				j++;
			}
		}
	}

}
