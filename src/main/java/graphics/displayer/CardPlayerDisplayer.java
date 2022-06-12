package graphics.displayer;

import static context.visuals.colour.Colour.rgba;

import java.util.ArrayList;
import java.util.List;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.GameCamera;
import context.visuals.builtin.RectangleRenderer;
import context.visuals.lwjgl.Texture;
import event.logicprocessing.CardPlayedEvent;
import graphics.displayable.ActorBodyPart;
import model.actor.CardPlayer;
import model.card.CardQueue;
import model.state.GameState;

public abstract class CardPlayerDisplayer<T extends CardPlayer> extends HealthActorDisplayer<T> {

	private ResourcePack resourcePack;

	private RectangleRenderer rectangleRenderer;

//	private Texture chainSegment;
//	private Texture effectSquare;

	protected List<ActorBodyPart> actorBodyParts = new ArrayList<>(2);

	public CardPlayerDisplayer(long actorID) {
		super(actorID);
	}

	@Override
	protected void init(ResourcePack resourcePack, GameState state) {
		super.init(resourcePack, state);
		this.resourcePack = resourcePack;
		rectangleRenderer = resourcePack.getRenderer("rectangle", RectangleRenderer.class);
//		chainSegment = resourcePack.getTexture("chain_segment");
//		effectSquare = resourcePack.getTexture("effect_square");
		font = resourcePack.getFont("langar");
	}

	protected final void displayQueue(GLContext glContext, NomadsSettings s, T t, GameState state, GameCamera camera) {
		float x = t.screenPos(camera, s).x;
		float y = t.screenPos(camera, s).y;
		rectangleRenderer.render(x + 10, y - 90, 120, 35, rgba(186, 157, 93, 230));
		CardQueue queue = t.cardDashboard().queue();
		for (int i = 0; i < queue.size(); i++) {
			CardPlayedEvent cpe = queue.get(i);
			Texture cardTex = resourcePack.getTexture("card_art_" + cpe.cardID().getFrom(state).name().replace(' ', '_').toLowerCase());
			textureRenderer.render(cardTex, x + 36 + i * 40, y - 40, 0.4f);
		}
	}

	protected final void displayEffectChains(GLContext glContext, NomadsSettings s, T t, GameState state, GameCamera camera) {
//		float x = t.screenPos(camera, s).x;
//		float y = t.screenPos(camera, s).y;
//		List<EffectChain> chains = t.chains();
//		for (int i = 0; i < chains.size(); i++) {
//			EffectChain chain = chains.get(i);
//			List<ChainEvent> toDisplay = chain.stream().filter(ChainEvent::shouldDisplay).collect(Collectors.toList());
//
//			float chainX = (x - (toDisplay.size() - 1) * 0.5f * 40);
//			float chainY = (y - 110 - i * (effectSquare.height() * 0.1f + 5));
//			for (int j = 0; j < toDisplay.size(); j++) {
////				ChainEvent event = toDisplay.get(j); TODO
//				textureRenderer.render(effectSquare, chainX + j * 50, chainY, 0.1f);
//				if (j != toDisplay.size() - 1) {
//					textureRenderer.render(chainSegment, chainX + effectSquare.width() * 0.05f + chainSegment.width() * 0.15f + j * 50,
//							chainY, 0.3f);
//				}
//			}
//		}
	}

	public ResourcePack resourcePack() {
		return resourcePack;
	}

	public RectangleRenderer rectangleRenderer() {
		return rectangleRenderer;
	}

}
