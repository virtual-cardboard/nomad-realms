package nomadrealms.render.ui;

import static common.colour.Colour.rgb;
import static common.colour.Colour.toRangedVector;
import static visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static visuals.constraint.posdim.MultiplierConstraint.factor;

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
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.UICard;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.zone.Deck;
import nomadrealms.game.zone.WorldCardZone;
import nomadrealms.render.RenderingEnvironment;
import visuals.builtin.RectangleVertexArrayObject;
import visuals.constraint.box.ConstraintBox;
import visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import visuals.lwjgl.render.meta.DrawFunction;

public class DeckTab implements UI {

	ConstraintBox constraintBox;
	Map<WorldCardZone, ConstraintBox> deckConstraints = new HashMap<>();
	Map<WorldCardZone, Map<WorldCard, UICard>> deckUICards = new HashMap<>();

	TargetingArrow targetingArrow;

	transient CardPlayer owner;

	transient UICard selectedCard;

	ConstraintBox screen;

	/**
	 *
	 */
	public DeckTab(CardPlayer owner, ConstraintBox screen,
				   TargetingArrow targetingArrow,
				   List<Consumer<MousePressedInputEvent>> onClick,
				   List<Consumer<MouseMovedInputEvent>> onDrag,
				   List<Consumer<MouseReleasedInputEvent>> onDrop) {
		this.owner = owner;
		this.targetingArrow = targetingArrow;
		this.screen = screen;
		constraintBox = new ConstraintBox(
				screen.x().add(screen.w().multiply(0.6f)),
				absolute(0),
				factor(screen.w(), 0.4f),
				screen.h()
		);
		ConstraintBox deck1Position = new ConstraintBox(
				constraintBox.x().add(factor(constraintBox.w(), 0.3f)),
				constraintBox.y().add(factor(constraintBox.h(), 0.3f)),
				UICard.cardSize(2)
		);
		ConstraintBox deck2Position = new ConstraintBox(
				constraintBox.x().add(factor(constraintBox.w(), 0.7f)),
				constraintBox.y().add(factor(constraintBox.h(), 0.3f)),
				UICard.cardSize(2)
		);
		ConstraintBox deck3Position = new ConstraintBox(
				constraintBox.x().add(factor(constraintBox.w(), 0.3f)),
				constraintBox.y().add(factor(constraintBox.h(), 0.7f)),
				UICard.cardSize(2)
		);
		ConstraintBox deck4Position = new ConstraintBox(
				constraintBox.x().add(factor(constraintBox.w(), 0.7f)),
				constraintBox.y().add(factor(constraintBox.h(), 0.7f)),
				UICard.cardSize(2)
		);
		deckConstraints.put(owner.deckCollection().deck1(), deck1Position);
		deckConstraints.put(owner.deckCollection().deck2(), deck2Position);
		deckConstraints.put(owner.deckCollection().deck3(), deck3Position);
		deckConstraints.put(owner.deckCollection().deck4(), deck4Position);
		for (Deck deck : owner.deckCollection().decks()) {
			Map<WorldCard, UICard> uiCards = new HashMap<>();
			uiCards.put(deck.peek(), new UICard(deck.peek(), deckConstraints.get(deck)));
			deckUICards.put(deck, uiCards);
		}

		addCallbacks(onClick, onDrag, onDrop);
	}

	private void addCallbacks(List<Consumer<MousePressedInputEvent>> onClick,
							  List<Consumer<MouseMovedInputEvent>> onDrag,
							  List<Consumer<MouseReleasedInputEvent>> onDrop) {
		onClick.add(
				(event) -> {
					selectedCard = cards()
							.filter(card -> card.physics().cardBox().contains(event.mouse().coordinate()))
							.findFirst()
							.orElse(null);
				}
		);
		onDrag.add(
				(event) -> {
					if (selectedCard != null) {
						if (selectedCard.needsTarget() && event.mouse().x() < constraintBox.x().get()) {
							selectedCard.move(constraintBox.x().get() - selectedCard.position().x() - 10, 0);
							targetingArrow.origin(selectedCard);
							targetingArrow.info(selectedCard.targetingInfo());
						} else {
							targetingArrow.origin(null);
							selectedCard.move(event.offsetX(), event.offsetY());
							selectedCard.tilt(new Vector2f(event.offsetX(), event.offsetY()));
						}
					}
				}
		);
		onDrop.add(
				(event) -> {
					if (selectedCard != null && selectedCard.position().x() < constraintBox.x().get()) {
						if (targetingArrow.target == null ^ selectedCard.needsTarget()) {
							owner.addNextPlay(new CardPlayedEvent(selectedCard.card(), owner, targetingArrow.target));
							selectedCard.physics().pauseRestoration = true;
						}
					}
					selectedCard = null;
					targetingArrow.origin(null);
					targetingArrow.target = null;
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
		cards().forEach(card -> card.render(re));
		cards().forEach(UICard::restoreOrientation);
		cards().filter(card -> card != selectedCard).forEach(UICard::restorePosition);
	}

	public Stream<UICard> cards() {
		return deckUICards.values().stream().flatMap(map -> map.values().stream());
	}

	public void deleteUI(WorldCard card) {
		deckUICards.get(card.zone()).remove(card);
	}

	public void addUI(WorldCard card) {
		deckUICards.get(card.zone()).put(card, new UICard(card, deckConstraints.get(card.zone())));
	}

}
