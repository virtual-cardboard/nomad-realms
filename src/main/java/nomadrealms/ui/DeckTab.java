package nomadrealms.ui;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static visuals.constraint.posdim.AbsolutePosDimConstraint.absolute;
import static visuals.constraint.posdim.MultiplierPosDimConstraint.factor;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import common.math.Matrix4f;
import common.math.Vector2f;
import context.input.event.MouseMovedInputEvent;
import context.input.event.MousePressedInputEvent;
import context.input.event.MouseReleasedInputEvent;
import nomadrealms.card.card.UICard;
import nomadrealms.card.card.WorldCard;
import nomadrealms.card.zone.DeckCollection;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.ConstraintBox;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class DeckTab extends UI {

	ConstraintBox constraintBox;
	DeckCollection deckCollection;
	Map<WorldCard, UICard> deck1UICards = new HashMap<>();
	Map<WorldCard, UICard> deck2UICards = new HashMap<>();
	Map<WorldCard, UICard> deck3UICards = new HashMap<>();
	Map<WorldCard, UICard> deck4UICards = new HashMap<>();

	UICard selectedCard;

	public DeckTab(DeckCollection deckCollection, ConstraintBox screen,
	               List<Consumer<MousePressedInputEvent>> onClick,
	               List<Consumer<MouseMovedInputEvent>> onDrag,
	               List<Consumer<MouseReleasedInputEvent>> onDrop) {
		this.deckCollection = deckCollection;
		constraintBox = new ConstraintBox(
				screen.x().add(screen.w().multiply(0.6f)),
				absolute(0),
				factor(screen.w(), 0.4f),
				screen.h()
		);
		ConstraintBox deck1Position = new ConstraintBox(
				constraintBox.x().add(factor(constraintBox.w(), 0.2f)),
				constraintBox.y().add(factor(constraintBox.h(), 0.2f)),
				UICard.size(screen, 2)
		);
		ConstraintBox deck2Position = new ConstraintBox(
				constraintBox.x().add(factor(constraintBox.w(), 0.6f)),
				constraintBox.y().add(factor(constraintBox.h(), 0.2f)),
				UICard.size(screen, 2)
		);
		ConstraintBox deck3Position = new ConstraintBox(
				constraintBox.x().add(factor(constraintBox.w(), 0.2f)),
				constraintBox.y().add(factor(constraintBox.h(), 0.6f)),
				UICard.size(screen, 2)
		);
		ConstraintBox deck4Position = new ConstraintBox(
				constraintBox.x().add(factor(constraintBox.w(), 0.6f)),
				constraintBox.y().add(factor(constraintBox.h(), 0.6f)),
				UICard.size(screen, 2)
		);
		deck1UICards.put(deckCollection.deck1().peek(), new UICard(deckCollection.deck1().peek(), deck1Position));
		deck2UICards.put(deckCollection.deck2().peek(), new UICard(deckCollection.deck2().peek(), deck2Position));
		deck3UICards.put(deckCollection.deck3().peek(), new UICard(deckCollection.deck3().peek(), deck3Position));
		deck4UICards.put(deckCollection.deck4().peek(), new UICard(deckCollection.deck4().peek(), deck4Position));

		addCallbacks(onClick, onDrag, onDrop);
	}

	private void addCallbacks(List<Consumer<MousePressedInputEvent>> onClick,
	                          List<Consumer<MouseMovedInputEvent>> onDrag,
	                          List<Consumer<MouseReleasedInputEvent>> onDrop) {
		onClick.add(
				(event) -> {
					selectedCard = cards()
							.filter(card -> card.basePosition.contains(event.mouse().coordinate()))
							.findFirst()
							.orElse(null);
				}
		);
		onDrag.add(
				(event) -> {
					if (selectedCard != null) {
						selectedCard.positionOffset = selectedCard.positionOffset.add(event.offsetX(),
								event.offsetY());
						selectedCard.tilt(new Vector2f(event.offsetX(), event.offsetY()));
					}
				}
		);
		onDrop.add(
				(event) -> {
					selectedCard = null;
				}
		);
	}

	@Override
	public void render(RenderingEnvironment re) {
		DefaultFrameBuffer.instance().render(
				() -> {
					re.defaultShaderProgram
							.set("color", toRangedVector(rgb(210, 180, 140)))
							.set("transform", new Matrix4f(constraintBox, re.glContext))
							.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
				}
		);
		renderCards(re);
		restoreCards();
	}

	public void renderCards(RenderingEnvironment re) {
		cards().forEach(card -> card.render(re));
	}

	private void restoreCards() {
		cards().forEach(UICard::restoreOrientation);
		cards().filter(card -> card != selectedCard).forEach(UICard::restorePosition);
	}

	public Stream<UICard> cards() {
		return Stream.of(deck1UICards, deck2UICards, deck3UICards, deck4UICards)
				.map(Map::values)
				.flatMap(Collection::stream);
	}

}
