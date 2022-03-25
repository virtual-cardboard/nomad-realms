package context.game.visuals.gui.dashboard;

import context.GLContext;
import context.ResourcePack;
import context.data.GameData;
import context.game.NomadsGameData;
import context.game.visuals.gui.CardGui;
import model.card.WorldCard;
import model.id.WorldCardID;

/**
 * The GUI of a card in the world.
 * 
 * @author Jay
 */
public class WorldCardGui extends CardGui {

	private WorldCardID cardID;

	public WorldCardGui(WorldCard card, ResourcePack resourcePack) {
		super(resourcePack, card.card());
		cardID = card.id();
	}

	@Override
	public void render(GLContext glContext, GameData data, float x, float y, float width, float height) {
		NomadsGameData nomadsData = (NomadsGameData) data;
		WorldCard card = cardID.getFrom(nomadsData.previousState());
		render(glContext, nomadsData.settings(), card.name(), card.text(), card.cost());
	}

	public WorldCardID cardID() {
		return cardID;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " ID: " + cardID.toLongID();
	}

}
