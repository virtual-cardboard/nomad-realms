package nomadrealms.ui;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.math.Matrix4f;
import nomadrealms.card.card.UICard;
import nomadrealms.card.card.WorldCard;
import nomadrealms.card.zone.Deck;
import nomadrealms.card.zone.DeckCollection;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class DeckTab extends UI {

	DeckCollection deckCollection;
	Map<WorldCard, UICard> deck1UICards = new HashMap<>();
	Map<WorldCard, UICard> deck2UICards = new HashMap<>();
	Map<WorldCard, UICard> deck3UICards = new HashMap<>();
	Map<WorldCard, UICard> deck4UICards = new HashMap<>();

	public DeckTab(DeckCollection deckCollection) {
		this.deckCollection = deckCollection;
		deck1UICards.put(deckCollection.deck1().peek(), new UICard(deckCollection.deck1().peek()));
		deck2UICards.put(deckCollection.deck2().peek(), new UICard(deckCollection.deck2().peek()));
		deck3UICards.put(deckCollection.deck3().peek(), new UICard(deckCollection.deck3().peek()));
		deck4UICards.put(deckCollection.deck4().peek(), new UICard(deckCollection.deck4().peek()));
	}

	@Override
	public void render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(rgb(210, 180, 140)))
							.set("transform", new Matrix4f(re.glContext.width() * 0.6f, 0,
									re.glContext.width() * 0.4f, re.glContext.height(), re.glContext))
							.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
				}
		);
		renderCards(re);
		renderDeck(deckCollection.deck2(), re, 0, 1);
		renderDeck(deckCollection.deck3(), re, 1, 0);
		renderDeck(deckCollection.deck4(), re, 1, 1);
	}

	public void renderCards(RenderingEnvironment re) {
		List<UICard> cards = new ArrayList<>();
		cards.addAll(deck1UICards.values());
		cards.addAll(deck2UICards.values());
		cards.addAll(deck3UICards.values());
		cards.addAll(deck4UICards.values());
		for (int i = 0; i < cards.size(); i++) {
			UICard card = cards.get(i);
			DefaultFrameBuffer.instance().render(
					() -> {
						re.defaultShaderProgram
								.set("color", toRangedVector(rgb(255, 255, 255)))
								.set("transform", new Matrix4f(re.glContext.width() * 0.2f, re.glContext.height() * 0.2f,
										re.glContext.width() * 0.2f * i, 0, re.glContext))
								.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
					}
			);
		}
	}

	private void renderDeck(Deck deck, RenderingEnvironment re, int x, int y) {
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(rgb(255, 255, 255)))
							.set("transform", new Matrix4f(re.glContext.width() * 0.2f, re.glContext.height() * 0.2f,
									re.glContext.width() * 0.2f * x, re.glContext.height() * 0.2f * y, re.glContext))
				}
		);
	}

}
