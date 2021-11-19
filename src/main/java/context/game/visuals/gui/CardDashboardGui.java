package context.game.visuals.gui;

import java.util.HashMap;
import java.util.Map;

import common.math.Vector2f;
import context.ResourcePack;
import context.visuals.gui.InvisibleGui;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import model.card.CardDashboard;
import model.card.GameCard;

public final class CardDashboardGui extends InvisibleGui {

	private HandGui hand;
	private DeckGui deck;
	private DiscardGui discard;
	private QueueGui queue;
	private Map<GameCard, CardGui> cardGuis = new HashMap<>();

	public CardDashboardGui(CardDashboard dashboard, ResourcePack resourcePack) {
		setWidth(new RelativeDimensionConstraint(1));
		setHeight(new RelativeDimensionConstraint(1));
		setPosX(new PixelPositionConstraint(0));
		setPosY(new PixelPositionConstraint(0));
		addChild(deck = new DeckGui(dashboard, resourcePack));
		addChild(queue = new QueueGui(resourcePack));
		addChild(discard = new DiscardGui(resourcePack));
		addChild(hand = new HandGui(resourcePack));
	}

	public void updateCardPositions() {
		hand.updateCardPositions();
		deck.updateCardPositions();
		discard.updateCardPositions();
		queue.updateCardPositions();
	}

	public void resetTargetPositions(Vector2f screenDimensions) {
		hand.resetTargetPositions(screenDimensions);
		deck.resetTargetPositions(screenDimensions);
		discard.resetTargetPositions(screenDimensions);
		queue.resetTargetPositions(screenDimensions);
	}

	public HandGui hand() {
		return hand;
	}

	public DeckGui deck() {
		return deck;
	}

	public DiscardGui discard() {
		return discard;
	}

	public QueueGui queue() {
		return queue;
	}

	public void putCardGui(GameCard card, CardGui cardGui) {
		cardGuis.put(card, cardGui);
	}

	public CardGui getCardGui(GameCard card) {
		return cardGuis.get(card);
	}

	public void removeCardGui(GameCard card) {
		cardGuis.remove(card);
	}

}