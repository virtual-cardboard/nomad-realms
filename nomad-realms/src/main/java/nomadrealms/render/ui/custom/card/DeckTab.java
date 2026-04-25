package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.zero;
import static engine.visuals.rendering.text.TextFormat.textFormat;

import engine.common.math.Matrix4f;
import engine.common.math.Vector2f;
import engine.common.math.Vector3f;
import engine.context.input.Mouse;
import engine.context.input.event.InputCallbackRegistry;
import engine.visuals.builtin.RectangleVertexArrayObject;
import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import engine.visuals.constraint.box.ConstraintPair;
import engine.visuals.lwjgl.render.meta.DrawFunction;
import engine.visuals.rendering.text.HorizontalAlign;
import engine.visuals.rendering.text.VerticalAlign;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.context.game.zone.WorldCardZone;
import nomadrealms.event.game.cardzone.CardZoneListener;
import nomadrealms.event.game.cardzone.event.RestockCardZoneEvent;
import nomadrealms.event.game.cardzone.event.SurfaceCardEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.ui.custom.indicator.ManaIndicator;

public class DeckTab implements UI, CardZoneListener<WorldCard> {

	ConstraintBox constraintBox;
	Map<WorldCardZone, ConstraintBox> deckConstraints = new HashMap<>();
	Map<WorldCardZone, Map<WorldCard, UICard>> deckUICards = new HashMap<>();
	Map<WorldCardZone, UnrevealedCardUI> deckUnrevealedUICards = new HashMap<>();

	java.util.LinkedHashMap<WorldCard, UICard> discardPileUICards = new java.util.LinkedHashMap<>();
	java.util.List<UICard> animatingRestockCards = new java.util.ArrayList<>();
	java.util.Set<Deck> waitingForRestockRefresh = new java.util.HashSet<>();

	transient CardPlayer owner;


	private ManaIndicator manaIndicator;

	transient UICard selectedCard;
	transient CardTransform selectedCardOriginalTransform;

	ConstraintBox screen;
	TargetingArrow targetingArrow;

	ConstraintBox discardZoneBox;

	/**
	 *
	 */
	public DeckTab(CardPlayer owner, ConstraintBox screen,
				   GameState state, Mouse mouse, InputCallbackRegistry registry) {
		this.owner = owner;
		this.screen = screen;
		constraintBox = new ConstraintBox(
				screen.x().add(screen.w().multiply(0.6f)),
				absolute(0),
				screen.w().multiply(0.4f),
				screen.h()
		);

		ConstraintBox deckAreaBox = new ConstraintBox(
				constraintBox.x(),
				constraintBox.y(),
				constraintBox.w(),
				constraintBox.h().multiply(0.66f)
		);

		discardZoneBox = new ConstraintBox(
				constraintBox.x().add(constraintBox.w().multiply(0.1f)),
				constraintBox.y().add(constraintBox.h().multiply(0.66f)),
				constraintBox.w().multiply(0.8f),
				constraintBox.h().multiply(0.3f)
		);

		this.manaIndicator = new ManaIndicator(owner, constraintBox);
		this.targetingArrow = new TargetingArrow(state, owner).mouse(mouse);
		ConstraintPair size = UICard.cardSize(2.5f);
		Constraint xPadding = deckAreaBox.w().add(size.x().multiply(2).neg()).multiply(0.25f);
		Constraint yPadding = deckAreaBox.h().add(size.y().multiply(2).neg()).multiply(0.25f);
		ConstraintBox deck1Position = new ConstraintBox(
				deckAreaBox.center().add(size.x().neg(), size.y().neg()).add(xPadding.neg(), yPadding.neg()),
				size
		);
		ConstraintBox deck2Position = new ConstraintBox(
				deckAreaBox.center().add(zero(), size.y().neg()).add(xPadding, yPadding.neg()),
				size
		);
		ConstraintBox deck3Position = new ConstraintBox(
				deckAreaBox.center().add(size.x().neg(), zero()).add(xPadding.neg(), yPadding),
				size
		);
		ConstraintBox deck4Position = new ConstraintBox(
				deckAreaBox.center().add(zero(), zero()).add(xPadding, yPadding),
				size
		);
		deckConstraints.put(owner.deckCollection().deck1(), deck1Position);
		deckConstraints.put(owner.deckCollection().deck2(), deck2Position);
		deckConstraints.put(owner.deckCollection().deck3(), deck3Position);
		deckConstraints.put(owner.deckCollection().deck4(), deck4Position);
		for (Deck deck : owner.deckCollection().decks()) {
			Map<WorldCard, UICard> uiCards = new HashMap<>();
			if (deck.size() > 0) {
				uiCards.put(deck.peek(), new UICard(deck.peek(), deckConstraints.get(deck)));
			}
			deckUICards.put(deck, uiCards);
			deckUnrevealedUICards.put(deck, new UnrevealedCardUI(deck, deckConstraints.get(deck)));
			deck.events().subscribe(this);
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
											screen.x().add(screen.h().multiply(0.02f)),
											screen.y().add(screen.h().multiply(0.02f))
									)
							);
							targetingArrow.origin(selectedCard);
							targetingArrow.info(selectedCard.targetingInfo());
						} else {
							targetingArrow.origin(null);
							selectedCard.physics().targetCoord(
									new ConstraintPair(
											event.mouse().coordinate().x().add(selectedCard.physics().cardBox().w().multiply(-0.5f)),
											event.mouse().coordinate().y().add(selectedCard.physics().cardBox().h().multiply(-0.5f))
									)
							);
							selectedCard.tilt(new Vector2f(event.offsetX(), event.offsetY()));
						}
					}
				});
		registry.registerOnDrop(
				(event) -> {
					if (selectedCard != null) {
						if (selectedCard.position().x().get() < constraintBox.x().get()
								&& (targetingArrow.target() == null ^ selectedCard.needsTarget())) {
							if (owner.mana() >= ((GameCard) selectedCard.card().card()).manaCost()) {
								owner.addNextPlay(new CardPlayedEvent(selectedCard.card(), owner, targetingArrow.target()));
								selectedCard.physics().pauseRestoration = true;
							} else {
								manaIndicator.triggerError();
								selectedCard.physics().targetTransform(selectedCardOriginalTransform);
								selectedCard.physics().pauseRestoration = false;
							}
						} else {
							selectedCard.physics().targetTransform(selectedCardOriginalTransform);
							selectedCard.physics().pauseRestoration = false;
						}
					}
					selectedCard = null;
					targetingArrow.origin(null);
					targetingArrow.target(null);
				});
	}

	@Override
	public void render(RenderingEnvironment re) {
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(210, 180, 140)))
				.set("transform", new Matrix4f(constraintBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		re.rectangleRenderer.render(
				discardZoneBox,
				10,
				rgb(180, 150, 110),
				0,
				0
		);

		manaIndicator.render(re);
		targetingArrow.render(re);
		deckUnrevealedUICards.values().forEach(ui -> ui.render(re));

		cards().forEach(card -> card.render(re));
		discardPileUICards.values().forEach(card -> card.render(re));
		animatingRestockCards.forEach(card -> card.render(re));

		cards().forEach(UICard::interpolate);
		discardPileUICards.values().forEach(UICard::interpolate);
		animatingRestockCards.forEach(UICard::interpolate);

		animatingRestockCards.removeIf(card -> card.physics().isLinearAnimationFinished());

		if (animatingRestockCards.isEmpty() && !waitingForRestockRefresh.isEmpty()) {
			for (Deck deck : waitingForRestockRefresh) {
				refresh(deck);
			}
			waitingForRestockRefresh.clear();
		}
	}

	public Stream<UICard> cards() {
		return deckUICards.values().stream().flatMap(map -> map.values().stream());
	}

	public void discardUI(WorldCard card) {
		UICard uiCard = deckUICards.get(card.deck()).remove(card);
		if (uiCard != null) {
			discardPileUICards.put(card, uiCard);

			// We snap it immediately to top of screen with proper dimension
			ConstraintPair center = new ConstraintPair(
				screen.x().add(screen.w().multiply(0.5f)),
				screen.y().add(uiCard.physics().cardBox().h().multiply(-1f))
			);

			uiCard.physics().currentTransform().position(center.add(uiCard.physics().cardBox().dimensions().scale(-0.5f)));

			// Random 3D orientation for current transform
			engine.common.math.UnitQuaternion random3DOrientation = new engine.common.math.UnitQuaternion()
					.rotateBy(new Vector3f(1, 0, 0), (float) (Math.random() * 360))
					.rotateBy(new Vector3f(0, 1, 0), (float) (Math.random() * 360))
					.rotateBy(new Vector3f(0, 0, 1), (float) (Math.random() * 360));
			uiCard.physics().currentTransform().orientation(random3DOrientation);

			// Target random position inside discard zone
			float randomX = (float) Math.random() * (discardZoneBox.w().get() - uiCard.physics().cardBox().w().get());
			float randomY = (float) Math.random() * (discardZoneBox.h().get() - uiCard.physics().cardBox().h().get());

			ConstraintPair targetPosition = discardZoneBox.coordinate().add(absolute(randomX), absolute(randomY));

			// Target face-up flat with random Z rotation
			engine.common.math.UnitQuaternion targetOrientation = new engine.common.math.UnitQuaternion()
					.rotateBy(new Vector3f(0, 0, 1), (float) (Math.random() * 360));

			CardTransform targetTransform = new CardTransform(targetOrientation, targetPosition, uiCard.physics().currentTransform().size());
			uiCard.physics().startLinearAnimation(targetTransform, 12, 1); // 12 frames = ~0.2 seconds at 60fps, 1 = ease-in
		}
	}

	public void deleteUI(WorldCard card) {
		deckUICards.get(card.deck()).remove(card);
	}

	public void addUI(WorldCard card) {
		UICard ui = new UICard(card, deckConstraints.get(card.deck()));
		ui.physics().rotate(new Vector3f(0, 1, 0), 181);
		deckUICards.get(card.deck()).put(card, ui);
	}

	public CardPlayer owner() {
		return owner;
	}

	/**
	 * Refreshes the deck tab UI, e.g. after surfacing a card.
	 */
	public void refresh() {
		for (Deck deck : owner.deckCollection().decks()) {
			refresh(deck);
		}
	}

	private void refresh(Deck deck) {
		Map<WorldCard, UICard> uiCards = deckUICards.get(deck);
		if (uiCards == null) {
			return;
		}
		uiCards.clear();
		if (deck.size() > 0) {
			addUI(deck.peek());
		}
	}

	@Override
	public void handle(SurfaceCardEvent<WorldCard> event) {
		if (owner.deckCollection().contains(event.card().zone())) {
			refresh();
		}
	}

	@Override
	public void handle(RestockCardZoneEvent<WorldCard> event) {
		if (event.zone() instanceof Deck && owner.deckCollection().contains((Deck) event.zone())) {
			Deck deck = (Deck) event.zone();

			// Find all UICards in discard pile that belong to this deck
			java.util.List<WorldCard> toRemove = new java.util.ArrayList<>();
			for (Map.Entry<WorldCard, UICard> entry : discardPileUICards.entrySet()) {
				if (entry.getKey().deck() == deck) {
					toRemove.add(entry.getKey());
					UICard uiCard = entry.getValue();

					// Move back to deck position
					ConstraintBox deckPosition = deckConstraints.get(deck);

					// Target face-down orientation
					engine.common.math.UnitQuaternion targetOrientation = new engine.common.math.UnitQuaternion()
							.rotateBy(new Vector3f(0, 1, 0), 181);

					CardTransform targetTransform = new CardTransform(targetOrientation, deckPosition.coordinate(), uiCard.physics().currentTransform().size());

					// Fly to deck slot in ~0.3s (18 frames) with deceleration (ease-out)
					uiCard.physics().startLinearAnimation(targetTransform, 18, 2);

					animatingRestockCards.add(uiCard);
				}
			}

			for (WorldCard card : toRemove) {
				discardPileUICards.remove(card);
			}

			waitingForRestockRefresh.add(deck);
		}
	}

}
