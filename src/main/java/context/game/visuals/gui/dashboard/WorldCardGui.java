package context.game.visuals.gui.dashboard;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.game.visuals.gui.CardGui;
import model.card.WorldCard;
import model.id.WorldCardID;
import model.state.GameState;

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

	public void render(GLContext glContext, NomadsSettings s, GameState state) {
		WorldCard card = cardID.getFrom(state);
		render(glContext, s, card.name(), card.text(), card.cost());
	}

	public WorldCardID cardID() {
		return cardID;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " ID: " + cardID.toLongID();
	}

}
