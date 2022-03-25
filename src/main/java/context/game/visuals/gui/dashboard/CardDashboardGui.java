package context.game.visuals.gui.dashboard;

import java.util.HashMap;
import java.util.Map;

import app.NomadsSettings;
import common.math.Vector2f;
import context.ResourcePack;
import context.visuals.gui.InvisibleGui;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import model.id.CardPlayerID;
import model.id.WorldCardID;

public final class CardDashboardGui extends InvisibleGui {

	private HandGui hand;
	private DeckGui deck;
	private DiscardGui discard;
	private QueueGui queue;

	private Map<WorldCardID, WorldCardGui> cardGuis = new HashMap<>();

	private CardPlayerID playerID;

	public CardDashboardGui(CardPlayerID playerID, ResourcePack resourcePack, NomadsSettings settings) {
		this.playerID = playerID;
		deck = new DeckGui(resourcePack, settings);
		queue = new QueueGui(resourcePack);
		discard = new DiscardGui(resourcePack, settings);
		hand = new HandGui(resourcePack);
		addChild(deck);
		addChild(queue);
		addChild(discard);
		addChild(hand);
		setWidth(new RelativeDimensionConstraint(1));
		setHeight(new RelativeDimensionConstraint(1));
		setPosX(new PixelPositionConstraint(0));
		setPosY(new PixelPositionConstraint(0));
	}

	public void resetTargetPositions(Vector2f screenDimensions, NomadsSettings settings) {
		hand.resetTargetPositions(screenDimensions, settings);
		deck.resetTargetPositions(screenDimensions, settings);
		discard.resetTargetPositions(screenDimensions, settings);
		queue.resetTargetPositions(screenDimensions, settings);
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

	public void putCardGui(WorldCardID cardID, WorldCardGui cardGui) {
		cardGuis.put(cardID, cardGui);
	}

	public WorldCardGui getCardGui(WorldCardID cardID) {
		return cardGuis.get(cardID);
	}

	public void removeCardGui(WorldCardID cardID) {
		cardGuis.remove(cardID);
	}

	public CardPlayerID playerID() {
		return playerID;
	}

}