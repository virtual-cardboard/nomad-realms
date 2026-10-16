package nomadrealms.render.ui.custom.card;

import static engine.common.colour.Colour.rgb;
import static engine.common.colour.Colour.toRangedVector;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.constraint.posdim.CustomSupplierConstraint.custom;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.UICard;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.zone.Deck;
import nomadrealms.context.game.zone.WorldCardZone;
import nomadrealms.event.game.cardzone.CardZoneListener;
import nomadrealms.event.game.cardzone.event.RestockCardZoneEvent;
import nomadrealms.event.game.cardzone.event.SurfaceCardEvent;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.ui.UI;
import nomadrealms.render.ui.custom.indicator.ManaIndicator;

public class DeckTab implements UI, CardZoneListener<WorldCard> {

	private static final float CARD_SCALE = 1.8f;
	private static final float PEEK_HEIGHT = 45.0f;

	private float currentTrayYOffset = 0;
	private float targetTrayYOffset = 0;
	private final Constraint trayYOffsetConstraint = custom("trayYOffset", () -> currentTrayYOffset);

	ConstraintBox constraintBox;
	DiscardUI discardUI;
	Map<WorldCardZone, ConstraintBox> deckConstraints = new HashMap<>();
	Map<WorldCardZone, Map<WorldCard, UICard>> deckUICards = new HashMap<>();
	Map<WorldCardZone, UnrevealedCardUI> deckUnrevealedUICards = new HashMap<>();

	transient CardPlayer owner;

	private ManaIndicator manaIndicator;

	transient UICard selectedCard;
	transient CardTransform selectedCardOriginalTransform;

	ConstraintBox screen;
	TargetingArrow targetingArrow;
	Consumer<InputEvent> actionEventChannel;
	Mouse mouse;

	public DeckTab(CardPlayer owner, ConstraintBox screen,
				   GameState state, Mouse mouse, InputCallbackRegistry registry, Consumer<InputEvent> actionEventChannel) {
		this.owner = owner;
		this.actionEventChannel = actionEventChannel;
		this.screen = screen;
		this.mouse = mouse;

		// Tray covers 80% screen width at the bottom, 20% height
		Constraint trayX = screen.w().multiply(0.10f);
		Constraint trayW = screen.w().multiply(0.80f);
		Constraint trayH = screen.h().multiply(0.20f);
		Constraint trayY = screen.h().multiply(0.80f).add(trayYOffsetConstraint);

		constraintBox = new ConstraintBox(
				trayX,
				trayY,
				trayW,
				trayH
		);

		ConstraintBox deckArea = new ConstraintBox(
				constraintBox.x(),
				constraintBox.y(),
				constraintBox.w().multiply(0.75f),
				constraintBox.h()
		);

		this.discardUI = new DiscardUI(new ConstraintBox(
				constraintBox.x().add(deckArea.w()),
				constraintBox.y(),
				constraintBox.w().multiply(0.25f),
				constraintBox.h()
		));

		this.manaIndicator = new ManaIndicator(owner, deckArea);
		this.targetingArrow = new TargetingArrow(state, owner).mouse(mouse);

		// Layout the 4 decks side by side inside deckArea
		ConstraintPair size = UICard.cardSize(CARD_SCALE);
		Constraint cardSpacing = deckArea.w().add(size.x().multiply(4).neg()).multiply(0.20f);
		Constraint cardYOffset = absolute(10);

		ConstraintBox deck1Position = new ConstraintBox(
				deckArea.x().add(cardSpacing),
				deckArea.y().add(cardYOffset),
				size
		);
		ConstraintBox deck2Position = new ConstraintBox(
				deckArea.x().add(cardSpacing.multiply(2)).add(size.x()),
				deckArea.y().add(cardYOffset),
				size
		);
		ConstraintBox deck3Position = new ConstraintBox(
				deckArea.x().add(cardSpacing.multiply(3)).add(size.x().multiply(2)),
				deckArea.y().add(cardYOffset),
				size
		);
		ConstraintBox deck4Position = new ConstraintBox(
				deckArea.x().add(cardSpacing.multiply(4)).add(size.x().multiply(3)),
				deckArea.y().add(cardYOffset),
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
		discardUI.addInitialCards(owner.deckCollection().discardZone().getCards());
		owner.deckCollection().discardZone().events().subscribe(this);

		// Start retracted by default
		float trayHeightVal = screen.h().multiply(0.20f).get();
		currentTrayYOffset = trayHeightVal - PEEK_HEIGHT;
		targetTrayYOffset = currentTrayYOffset;

		addCallbacks(registry);
	}

	private void addCallbacks(InputCallbackRegistry registry) {
		registry.registerOnPress(
				(event) -> {
					if (event.button() == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT) {
						selectedCard = cards()
								.filter(card -> card.physics().cardBox().contains(event.mouse().coordinate()))
								.findFirst()
								.orElse(null);
						if (selectedCard != null) {
							selectedCardOriginalTransform = selectedCard.physics().targetTransform().copy();
						}
					} else if (event.button() == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
						if (selectedCard != null) {
							selectedCard.physics().targetTransform(selectedCardOriginalTransform);
							selectedCard.physics().pauseRestoration = false;
							selectedCard = null;
							targetingArrow.origin(null);
							targetingArrow.target(null);
						}
					}
				});
		registry.registerOnDrag(
				(event) -> {
					if (selectedCard != null) {
						float baseTrayTopY = screen.h().multiply(0.80f).get();
						if (selectedCard.needsTarget() && event.mouse().y() < baseTrayTopY) {
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
					if (event.button() == org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT) {
						if (selectedCard != null) {
							boolean played = false;
							float baseTrayTopY = screen.h().multiply(0.80f).get();
							if (selectedCard.position().y().get() < baseTrayTopY
									&& (targetingArrow.target() == null ^ selectedCard.needsTarget())) {
								if (owner.mana() >= ((GameCard) selectedCard.card().card()).manaCost()) {
									actionEventChannel.accept(new CardPlayedEvent(selectedCard.card(), owner, targetingArrow.target()));
									selectedCard.physics().pauseRestoration = true;
									played = true;
								} else {
									manaIndicator.triggerError();
								}
							}

							if (!played) {
								selectedCard.physics().targetTransform(selectedCardOriginalTransform);
								selectedCard.physics().pauseRestoration = false;
							}
						}
						selectedCard = null;
						targetingArrow.origin(null);
						targetingArrow.target(null);
					}
				});
	}

	private void updateTrayState() {
		float mouseX = mouse.x();
		float mouseY = mouse.y();

		float trayXMin = constraintBox.x().get();
		float trayXMax = trayXMin + constraintBox.w().get();
		float currentTrayY = constraintBox.y().get();
		float screenBottom = screen.h().get();
		float trayHeight = constraintBox.h().get();

		// Bounding box includes current tray bounds down to screen bottom
		boolean mouseInTray = mouseX >= trayXMin && mouseX <= trayXMax && mouseY >= (currentTrayY - 10) && mouseY <= screenBottom;

		if (mouseInTray || selectedCard != null) {
			targetTrayYOffset = 0; // Extended state
		} else {
			targetTrayYOffset = trayHeight - PEEK_HEIGHT; // Retracted state
		}

		currentTrayYOffset += (targetTrayYOffset - currentTrayYOffset) * 0.15f;
	}

	@Override
	public void render(RenderingEnvironment re) {
		updateTrayState();

		discardUI.processRestockTasks(task -> deckUICards.get(task.deck).put(task.card, task.ui));

		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(210, 180, 140)))
				.set("transform", new Matrix4f(constraintBox, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		discardUI.renderBackground(re);
		manaIndicator.render(re);
		targetingArrow.render(re);
		deckUnrevealedUICards.values().forEach(ui -> ui.render(re));
		cards().forEach(card -> card.render(re));
		discardUI.renderCards(re);
		discardUI.updateAnimations();
		cards().forEach(card -> {
			if (discardUI.discardArea().contains(card.position().vector())) {
				// Card is still in discard area, keep its size
				card.physics().interpolate(0.1f);
			} else {
				// Card is flying back or in deck, use normal interpolation
				card.physics().interpolate();
			}
		});
	}

	public Stream<UICard> cards() {
		return deckUICards.values().stream().flatMap(map -> map.values().stream());
	}

	public void deleteUI(WorldCard card) {
		deckUICards.get(card.deck()).remove(card);
		discardUI.addCard(card);
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
			discardUI.restockDeck(deck, deckConstraints.get(deck));
		}
	}

}
