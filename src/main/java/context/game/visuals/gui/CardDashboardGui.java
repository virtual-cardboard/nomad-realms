package context.game.visuals.gui;

import java.util.HashMap;
import java.util.Map;

import common.math.PosDim;
import common.math.Vector2f;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.RootGui;
import model.card.CardDashboard;
import model.state.GameState;

public final class CardDashboardGui {

	private RootGui rootGui;

	private HandGui hand;
	private DeckGui deck;
	private DiscardGui discard;
	private QueueGui queue;

	private Map<Long, CardGui> cardGuis = new HashMap<>();

	public CardDashboardGui(RootGui rootGui, CardDashboard dashboard, ResourcePack resourcePack) {
		this.rootGui = rootGui;
		deck = new DeckGui(dashboard, resourcePack);
		queue = new QueueGui(resourcePack);
		discard = new DiscardGui(resourcePack);
		hand = new HandGui(resourcePack);
		deck.setParent(this);
		queue.setParent(this);
		discard.setParent(this);
		hand.setParent(this);
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

	public void render(GLContext glContext, Vector2f screenDim, GameState state) {
		CardZoneGui[] zones = { deck, queue, discard, hand };
		for (CardZoneGui zone : zones) {
			PosDim p = zone.posdim();
			zone.doRender(glContext, screenDim, state, p.x, p.y, p.w, p.h);
		}
	}

	PosDim posdim() {
		return rootGui.posdim();
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

	public void putCardGui(long cardID, CardGui cardGui) {
		cardGuis.put(cardID, cardGui);
	}

	public CardGui getCardGui(long cardID) {
		return cardGuis.get(cardID);
	}

	public void removeCardGui(long cardID) {
		cardGuis.remove(cardID);
	}

}