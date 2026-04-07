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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
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
	ConstraintBox discardArea;
	Map<WorldCardZone, ConstraintBox> deckConstraints = new HashMap<>();
	Map<WorldCardZone, ConcurrentHashMap<WorldCard, UICard>> deckUICards = new HashMap<>();
	Map<WorldCardZone, UnrevealedCardUI> deckUnrevealedUICards = new HashMap<>();
	List<UICard> discardUICards = Collections.synchronizedList(new ArrayList<>());

	transient CardPlayer owner;

	private ManaIndicator manaIndicator;

	transient UICard selectedCard;
	transient CardTransform selectedCardOriginalTransform;

	ConstraintBox screen;
	TargetingArrow targetingArrow;

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
		this.manaIndicator = new ManaIndicator(owner, constraintBox);
		this.targetingArrow = new TargetingArrow(state, owner).mouse(mouse);

		ConstraintBox deckArea = new ConstraintBox(
				constraintBox.x(),
				constraintBox.y(),
				constraintBox.w(),
				constraintBox.h().multiply(0.7f)
		);
		this.discardArea = new ConstraintBox(
				constraintBox.x(),
				constraintBox.y().add(deckArea.h()),
				constraintBox.w(),
				constraintBox.h().multiply(0.3f)
		);

		ConstraintPair size = UICard.cardSize(2.5f);
		Constraint xPadding = deckArea.w().add(size.x().multiply(2).neg()).multiply(0.25f);
		Constraint yPadding = deckArea.h().add(size.y().multiply(2).neg()).multiply(0.25f);
		ConstraintBox deck1Position = new ConstraintBox(
				deckArea.center().add(size.x().neg(), size.y().neg()).add(xPadding.neg(), yPadding.neg()),
				size
		);
		ConstraintBox deck2Position = new ConstraintBox(
				deckArea.center().add(zero(), size.y().neg()).add(xPadding, yPadding.neg()),
				size
		);
		ConstraintBox deck3Position = new ConstraintBox(
				deckArea.center().add(size.x().neg(), zero()).add(xPadding.neg(), yPadding),
				size
		);
		ConstraintBox deck4Position = new ConstraintBox(
				deckArea.center().add(zero(), zero()).add(xPadding, yPadding),
				size
		);
		deckConstraints.put(owner.deckCollection().deck1(), deck1Position);
		deckConstraints.put(owner.deckCollection().deck2(), deck2Position);
		deckConstraints.put(owner.deckCollection().deck3(), deck3Position);
		deckConstraints.put(owner.deckCollection().deck4(), deck4Position);
		for (Deck deck : owner.deckCollection().decks()) {
			ConcurrentHashMap<WorldCard, UICard> uiCards = new ConcurrentHashMap<>();
			if (deck.size() > 0) {
				uiCards.put(deck.peek(), new UICard(deck.peek(), deckConstraints.get(deck)));
			}
			deckUICards.put(deck, uiCards);
			deckUnrevealedUICards.put(deck, new UnrevealedCardUI(deck, deckConstraints.get(deck)));
			deck.events().subscribe(this);
		}
		for (WorldCard card : owner.deckCollection().discardZone().getCards()) {
			ConstraintPair cardSize = UICard.cardSize(2.5f);
			ConstraintBox cardBox = new ConstraintBox(
					discardArea.center().add(cardSize.scale(-0.5f)),
					cardSize
			);
			UICard ui = new UICard(card, cardBox);
			ui.physics().rotate(new Vector3f(0, 0, 1), (float) (Math.random() * 40 - 20));
			ui.physics().snap();
			discardUICards.add(ui);
		}
		owner.deckCollection().discardZone().events().subscribe(this);

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
		re.defaultShaderProgram
				.set("color", toRangedVector(rgb(180, 150, 110)))
				.set("transform", new Matrix4f(discardArea, re.glContext))
				.use(new DrawFunction().vao(RectangleVertexArrayObject.instance()).glContext(re.glContext));
		manaIndicator.render(re);
		targetingArrow.render(re);
		deckUnrevealedUICards.values().forEach(ui -> ui.render(re));
		discardUICards.forEach(card -> card.render(re));
		cards().forEach(card -> card.render(re));
		discardUICards.forEach(card -> {
			float targetY = discardArea.center().y().get() - card.physics().cardBox().h().multiply(0.5f).get();
			float t = (targetY != 0) ? card.position().y().get() / targetY : 1;
			t = Math.max(0, Math.min(1, t));
			card.physics().targetTransform().size(UICard.cardSize(2.5f).scale(1 + 2 * (1 - t)));
			card.physics().interpolate(0.2f);
		});
		cards().forEach(card -> {
			if (discardArea.contains(card.position().vector())) {
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
		ConstraintPair cardSize = UICard.cardSize(2.5f);
		ConstraintBox targetBox = new ConstraintBox(
				discardArea.center().add(cardSize.scale(-0.5f)),
				cardSize
		);
		ConstraintBox tempBox = new ConstraintBox(
				discardArea.center().add(cardSize.scale(-1.5f)),
				cardSize.scale(3)
		);
		ConstraintBox startBox = new ConstraintBox(
				tempBox.x(), absolute(-tempBox.h().get()),
				tempBox.w(), tempBox.h()
		);

		UICard ui = new UICard(card, startBox);
		ui.physics().rotate(new Vector3f((float) Math.random(), (float) Math.random(), (float) Math.random()), (float) (Math.random() * 360));
		ui.physics().targetTransform(new CardTransform(
				new engine.common.math.UnitQuaternion().rotateBy(new Vector3f(0, 0, 1), (float) (Math.random() * 40 - 20)),
				targetBox
		));
		discardUICards.add(ui);
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
		ConcurrentHashMap<WorldCard, UICard> uiCards = deckUICards.get(deck);
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
			List<UICard> toRestock = discardUICards.stream()
					.filter(ui -> ui.card().deck() == deck)
					.collect(Collectors.toList());
			discardUICards.removeAll(toRestock);
			for (int i = 0; i < toRestock.size(); i++) {
				UICard ui = toRestock.get(i);
				ui.physics().targetTransform(new CardTransform(
						new engine.common.math.UnitQuaternion(),
						deckConstraints.get(deck)
				));
				long delay = i * 100;
				new Thread(() -> {
					try {
						Thread.sleep(delay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ConcurrentHashMap<WorldCard, UICard> deckMap = deckUICards.get(deck);
					deckMap.put(ui.card(), ui);
				}).start();
			}
		}
	}

}
