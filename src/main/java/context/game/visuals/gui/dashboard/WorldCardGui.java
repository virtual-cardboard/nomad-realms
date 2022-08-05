package context.game.visuals.gui.dashboard;

import static app.NomadsSettings.ENLARGED_CARD_SIZE_FACTOR;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.game.NomadsGameData;
import context.game.visuals.gui.CardGui;
import model.card.WorldCard;
import model.id.WorldCardId;

/**
 * The GUI of a card in the world.
 *
 * @author Jay
 */
public class WorldCardGui extends CardGui {

	private WorldCardId cardID;

	public WorldCardGui(WorldCard card, ResourcePack resourcePack) {
		super(resourcePack, card.card());
		cardID = card.id();
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		NomadsGameData nomadsData = (NomadsGameData) data;
		WorldCard card = cardID.getFrom(nomadsData.previousState());
		render(glContext, nomadsData, nomadsData.settings(), card.name(), card.text(), card.cost());
	}

	public WorldCardId cardID() {
		return cardID;
	}

	public void hover(NomadsSettings settings) {
		if (!hovered) {
			centerPos = centerPos.add(0, -settings.cardHeight() * (ENLARGED_CARD_SIZE_FACTOR - 1) * 0.5f);
			setTargetPos(targetPos.x(), targetPos.y() - settings.cardHeight() * 0.3f);
			targetScale = ENLARGED_CARD_SIZE_FACTOR;
			hovered = true;
		}
	}

	public void unhover(NomadsSettings settings) {
		if (hovered) {
			centerPos = centerPos.add(0, settings.cardHeight() * (ENLARGED_CARD_SIZE_FACTOR - 1) * 0.5f);
			targetScale = 1;
			hovered = false;
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " ID: " + cardID.toLongID();
	}

}
