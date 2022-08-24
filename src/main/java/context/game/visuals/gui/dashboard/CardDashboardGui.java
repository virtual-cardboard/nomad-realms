package context.game.visuals.gui.dashboard;

import java.util.HashMap;
import java.util.Map;

import app.NomadsSettings;
import context.ResourcePack;
import context.visuals.gui.InvisibleGui;
import context.visuals.gui.RootGui;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import engine.common.math.Vector2f;
import event.logicprocessing.CardPlayedEvent;
import model.actor.CardPlayer;
import model.card.WorldCard;
import model.id.CardPlayerId;
import model.id.WorldCardId;
import model.state.GameState;

public final class CardDashboardGui extends InvisibleGui {

	private HandGui hand;
	private DeckGui deck;
	private DiscardGui discard;
	private QueueGui queue;

	private Map<WorldCardId, WorldCardGui> cardGuis = new HashMap<>();

	private CardPlayerId playerID;

	public CardDashboardGui(ResourcePack resourcePack, NomadsSettings settings) {
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

	public void putCardGui(WorldCardId cardID, WorldCardGui cardGui) {
		cardGuis.put(cardID, cardGui);
	}

	public WorldCardGui getCardGui(WorldCardId cardID) {
		return cardGuis.get(cardID);
	}

	public void removeCardGui(WorldCardId cardID) {
		cardGuis.remove(cardID);
	}

	public CardPlayerId playerID() {
		return playerID;
	}

	public void setPlayerID(CardPlayerId playerID) {
		this.playerID = playerID;
	}

	public static CardDashboardGui fromCardPlayer(ResourcePack resourcePack, NomadsSettings settings, RootGui rootGui, GameState state, CardPlayerId playerID) {
		CardDashboardGui dashboardGui = new CardDashboardGui(resourcePack, settings);
		dashboardGui.setPlayerID(playerID);
		CardPlayer player = playerID.getFrom(state);
		for (WorldCard card : player.cardDashboard().hand()) {
			dashboardGui.hand().addChild(new WorldCardGui(card, resourcePack));
		}
		for (CardPlayedEvent card : player.cardDashboard().queue()) {
			dashboardGui.queue().addChild(new WorldCardGui(card.cardID().getFrom(state), resourcePack));
		}
		for (WorldCard card : player.cardDashboard().discard()) {
			dashboardGui.discard().addChild(new WorldCardGui(card, resourcePack));
		}
		dashboardGui.setParent(rootGui);
		dashboardGui.resetTargetPositions(rootGui.dimensions(), settings);
		dashboardGui.setEnabled(true);
		return dashboardGui;
	}

}
