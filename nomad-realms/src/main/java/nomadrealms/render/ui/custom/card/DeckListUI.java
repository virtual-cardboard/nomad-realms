package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.rendering.text.HorizontalAlign.LEFT;
import static engine.visuals.rendering.text.TextFormat.textFormat;
import static engine.visuals.rendering.text.VerticalAlign.TOP;

import java.util.ArrayList;
import java.util.List;

import engine.context.input.event.InputCallbackRegistry;
import engine.common.math.Matrix4f;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.collection.DeckList;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.content.BasicUIContent;
import nomadrealms.render.ui.content.UIContent;

public class DeckListUI extends BasicUIContent {

	private final String title;
	private final DeckList deckList;
	private final List<DeckListCardUI> cardUIs = new ArrayList<>();
	private boolean selected = false;

	public DeckListUI(UIContent parent, String title, DeckList deckList, ConstraintBox box) {
		this(parent, title, deckList, box, null);
	}

	public DeckListUI(UIContent parent, String title, DeckList deckList, ConstraintBox box, InputCallbackRegistry registry) {
		super(parent, box);
		this.title = title;
		this.deckList = deckList;
		rebuildCardUIs();
	}

	private void rebuildCardUIs() {
		clearChildren();
		cardUIs.clear();

		List<GameCard> cards = deckList.getCards();
		float cardStripHeight = 25.0f;
		float cardStripPadding = 4.0f;
		float startY = 40.0f;
		for (int j = 0; j < cards.size(); j++) {
			GameCard card = cards.get(j);
			ConstraintBox stripBox = new ConstraintBox(
					constraintBox().x().add(absolute(10)),
					constraintBox().y().add(absolute(startY + j * (cardStripHeight + cardStripPadding))),
					constraintBox().w().add(absolute(-20)),
					absolute(cardStripHeight)
			);
			DeckListCardUI cardUI = new DeckListCardUI(this, card, stripBox);
			cardUIs.add(cardUI);
			addChild(cardUI);
		}
	}

	public void addCard(GameCard card) {
		deckList.addCard(card);
		rebuildCardUIs();
	}

	public void removeCard(GameCard card) {
		deckList.removeCard(card);
		rebuildCardUIs();
	}

	public boolean selected() {
		return selected;
	}

	public DeckListUI selected(boolean selected) {
		this.selected = selected;
		return this;
	}

	public DeckList deckList() {
		return deckList;
	}

	@Override
	public void _render(RenderingEnvironment re) {
		// Render background box: 60,60,60 if selected, 50,50,50 if unselected
		int bgColour = selected ? rgb(60, 60, 60) : rgb(50, 50, 50);
		re.defaultShaderProgram
				.set("color", toRangedVector(bgColour))
				.set("transform", new Matrix4f(constraintBox(), re.glContext))
				.use(new DrawFunction()
						.vao(RectangleVertexArrayObject.instance())
						.glContext(re.glContext));

		// Render Title Text
		re.textRenderer.render(
				textFormat()
						.text(title)
						.font(re.font)
						.fontSize(20)
						.colour(rgb(255, 255, 255))
						.hAlign(LEFT)
						.vAlign(TOP)
						.transform(re.textRenderer.screenToPixel().copy().translate(
								constraintBox().x().get() + 10,
								constraintBox().y().get() + 10))
		);
	}

	public List<DeckListCardUI> cardUIs() {
		return cardUIs;
	}

}
