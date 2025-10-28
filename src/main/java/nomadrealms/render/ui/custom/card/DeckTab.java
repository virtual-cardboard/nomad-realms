package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.context.input.event.MouseMovedInputEvent;
import engine.context.input.event.MousePressedInputEvent;
import engine.context.input.event.MouseReleasedInputEvent;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.lwjgl.render.framebuffer.DefaultFrameBuffer;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.game.actor.cardplayer.CardPlayer;
import nomadrealms.game.card.UICard;
import nomadrealms.game.card.WorldCard;
import nomadrealms.game.event.CardPlayedEvent;
import nomadrealms.game.zone.Deck;
import nomadrealms.game.zone.WorldCardZone;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

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
				screen.w().multiply(0.4f),
				screen.h()
		);
		ConstraintBox deck1Position = new ConstraintBox(
				constraintBox.x().add(constraintBox.w().multiply(0.3f)),
				constraintBox.y().add(constraintBox.h().multiply(0.3f)),
				UICard.cardSize(2)
		);
		ConstraintBox deck2Position = new ConstraintBox(
				constraintBox.x().add(constraintBox.w().multiply(0.7f)),
				constraintBox.y().add(constraintBox.h().multiply(0.3f)),
				UICard.cardSize(2)
		);
		ConstraintBox deck3Position = new ConstraintBox(
				constraintBox.x().add(constraintBox.w().multiply(0.3f)),
				constraintBox.y().add(constraintBox.h().multiply(0.7f)),
				UICard.cardSize(2)
		);
		ConstraintBox deck4Position = new ConstraintBox(
				constraintBox.x().add(constraintBox.w().multiply(0.7f)),
				constraintBox.y().add(constraintBox.h().multiply(0.7f)),
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
							selectedCard.move(constraintBox.x().get() - selectedCard.position().x().get() - 10, 0);
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
					if (selectedCard != null && selectedCard.position().x().get() < constraintBox.x().get()) {
						if (targetingArrow.target() == null ^ selectedCard.needsTarget()) {
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
		cards().forEach(UICard::interpolate);
		cards().filter(card -> card != selectedCard).forEach(UICard::interpolate);
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

	public CardPlayer owner() {
		return owner;
	}
}
