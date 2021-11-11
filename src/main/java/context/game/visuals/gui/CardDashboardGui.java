package context.game.visuals.gui;

import java.util.ArrayList;
import java.util.List;

import common.math.Vector2f;
import context.ResourcePack;
import context.visuals.gui.Gui;
import context.visuals.gui.InvisibleGui;
import context.visuals.gui.constraint.dimension.RelativeDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import model.card.CardDashboard;

public final class CardDashboardGui extends InvisibleGui {

	private List<CardZoneGui> cardZoneGuis = new ArrayList<>(4);
	private HandGui hand;
	private DeckGui deck;
	private DiscardGui discard;

	public CardDashboardGui(CardDashboard dashboard, ResourcePack resourcePack) {
		setWidth(new RelativeDimensionConstraint(1));
		setHeight(new RelativeDimensionConstraint(1));
		setPosX(new PixelPositionConstraint(0));
		setPosY(new PixelPositionConstraint(0));
		addChild(hand = new HandGui(resourcePack));
		addChild(deck = new DeckGui(dashboard, resourcePack));
		addChild(discard = new DiscardGui(dashboard, resourcePack));
	}

	@Override
	public void addChild(Gui child) {
		if (child instanceof CardZoneGui) {
			super.addChild(child);
			cardZoneGuis.add((CardZoneGui) child);
			return;
		}
		throw new RuntimeException("Gui " + child.getClass().getSimpleName() + " cannot be a child of CardDashboardGui.");
	}

	public void updateCardPositions() {
		for (CardZoneGui cardZoneGui : cardZoneGuis) {
			cardZoneGui.updateCardPositions();
		}
	}

	public void resetTargetPositions(Vector2f screenDimensions) {
		for (CardZoneGui cardZoneGui : cardZoneGuis) {
			cardZoneGui.resetTargetPositions(screenDimensions);
		}
	}

	public List<CardZoneGui> cardZoneGuis() {
		return cardZoneGuis;
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

}