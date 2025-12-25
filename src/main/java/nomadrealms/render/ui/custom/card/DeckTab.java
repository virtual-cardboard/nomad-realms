package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.zero;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import nomadrealms.context.game.actor.cardplayer.CardPlayer;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.context.game.zone.WorldCardZone;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;

public class DeckTab implements UI {

	ConstraintBox constraintBox;
	Map<WorldCardZone, ConstraintBox> deckConstraints = new HashMap<>();
	Map<WorldCardZone, Map<WorldCard, UICard>> deckUICards = new HashMap<>();
	Map<WorldCardZone, UnrevealedCardUI> deckUnrevealedUICards = new HashMap<>();

	TargetingArrow targetingArrow;

	transient CardPlayer owner;

	transient UICard selectedCard;
	transient CardTransform selectedCardOriginalTransform;

	ConstraintBox screen;

	/**
	 *
	 */
	public DeckTab(CardPlayer owner, ConstraintBox screen,
				   TargetingArrow targetingArrow, InputCallbackRegistry registry) {
		this.owner = owner;
		this.targetingArrow = targetingArrow;
		this.screen = screen;
		constraintBox = new ConstraintBox(
				screen.x().add(screen.w().multiply(0.6f)),
				absolute(0),
				screen.w().multiply(0.4f),
				screen.h()
		);
		ConstraintPair size = UICard.cardSize(2);
		Constraint xPadding = constraintBox.w().add(size.x().multiply(2).neg()).multiply(0.25f);
		Constraint yPadding = constraintBox.h().add(size.y().multiply(2).neg()).multiply(0.25f);
		ConstraintBox deck1Position = new ConstraintBox(
				constraintBox.center().add(size.x().neg(), size.y().neg()).add(xPadding.neg(), yPadding.neg()),
				size
		);
		ConstraintBox deck2Position = new ConstraintBox(
				constraintBox.center().add(zero(), size.y().neg()).add(xPadding, yPadding.neg()),
				size
		);
		ConstraintBox deck3Position = new ConstraintBox(
				constraintBox.center().add(size.x().neg(), zero()).add(xPadding.neg(), yPadding),
				size
		);
		ConstraintBox deck4Position = new ConstraintBox(
				constraintBox.center().add(zero(), zero()).add(xPadding, yPadding),
				size
		);
		deckConstraints.put(owner.deckCollection().deck1(), deck1Position);
		deckConstraints.put(owner.deckCollection().deck2(), deck2Position);
		deckConstraints.put(owner.deckCollection().deck3(), deck3Position);
		deckConstraints.put(owner.deckCollection().deck4(), deck4Position);
		for (Deck deck : owner.deckCollection().decks()) {
			Map<WorldCard, UICard> uiCards = new HashMap<>();
			uiCards.put(deck.peek(), new UICard(deck.peek(), deckConstraints.get(deck)));
			deckUICards.put(deck, uiCards);
			deckUnrevealedUICards.put(deck, new UnrevealedCardUI(deck, deckConstraints.get(deck)));
		}

		addCallbacks(registry);
	}

	private void addCallbacks(InputCallbackRegistry registry) {
		registry.registerOnPress(
				(event) -> {
					selectedCard = cards()
							.filter(card -> card.physics().cardBox().contains(event.mouse().coordinate()))
							.findFirst()
							.orElse(null);
					if (selectedCard != null) {
						selectedCardOriginalTransform = selectedCard.physics().targetTransform().copy();
					}
				});
		registry.registerOnDrag(
				(event) -> {
					if (selectedCard != null) {
						if (selectedCard.needsTarget() && event.mouse().x() < constraintBox.x().get()) {
							selectedCard.physics().targetCoord(
									new ConstraintPair(
											constraintBox.x().add(selectedCard.physics().cardBox().w().neg()).add(absolute(-10)),
											event.mouse().coordinate().y().add(selectedCard.physics().cardBox().h().multiply(-0.5f))
									)
							);
							targetingArrow.origin(selectedCard);
							targetingArrow.info(selectedCard.targetingInfo());
						} else {
							targetingArrow.origin(null);
							selectedCard.move(event.offsetX(), event.offsetY());
							selectedCard.tilt(new Vector2f(event.offsetX(), event.offsetY()));
						}
					}
				});
		registry.registerOnDrop(
				(event) -> {
					if (selectedCard != null) {
						if (selectedCard.position().x().get() < constraintBox.x().get() && (targetingArrow.target() == null ^ selectedCard.needsTarget())) {
							owner.addNextPlay(new CardPlayedEvent(selectedCard.card(), owner, targetingArrow.target));
							selectedCard.physics().pauseRestoration = true;
						} else {
							selectedCard.physics().targetTransform(selectedCardOriginalTransform);
							selectedCard.physics().pauseRestoration = false;
						}
					}
					selectedCard = null;
					targetingArrow.origin(null);
					targetingArrow.target = null;
				});
	}

	@Override
	public void render(RenderingEnvironment re) {
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(210, 180, 140)))
				.set("transform", new Matrix4f(constraintBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		deckUnrevealedUICards.values().forEach(ui -> ui.render(re));
		cards().forEach(card -> card.render(re));
		cards().forEach(UICard::interpolate);
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
