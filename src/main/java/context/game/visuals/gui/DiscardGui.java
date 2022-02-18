package context.game.visuals.gui;

import static context.game.visuals.gui.CardGui.HEIGHT;
import static context.game.visuals.gui.CardGui.WIDTH;
import static model.card.CardType.ACTION;
import static model.card.CardType.CANTRIP;
import static model.card.CardType.CREATURE;
import static model.card.CardType.STRUCTURE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import context.visuals.lwjgl.Texture;
import model.card.CardType;
import model.state.GameState;

public class DiscardGui extends CardZoneGui {

	private Map<CardType, Texture> decorations = new HashMap<>(4, 1);

	public DiscardGui(ResourcePack resourcePack) {
		decorations.put(ACTION, resourcePack.getTexture("card_decoration_action"));
		decorations.put(CANTRIP, resourcePack.getTexture("card_decoration_cantrip"));
		decorations.put(CREATURE, resourcePack.getTexture("card_decoration_creature"));
		decorations.put(STRUCTURE, resourcePack.getTexture("card_decoration_structure"));
		setWidth(new PixelDimensionConstraint(WIDTH));
		setHeight(new PixelDimensionConstraint(HEIGHT));
		setPosX(new PixelPositionConstraint(-10));
		setPosY(new PixelPositionConstraint(-HEIGHT * 0.15f, height()));
	}

	@Override
	public void render(GLContext glContext, NomadsSettings s, GameState state, float x, float y, float width, float height) {
		boolean cardGuiOnTop = false;
		List<CardGui> cardGuis = cardGuis();
		for (int i = cardGuis.size() - 2; i >= 0; i--) {
			if (cardGuiOnTop) {
				removeCardGui(i);
			} else if (cardGuis.get(i).inPlace()) {
				cardGuiOnTop = true;
			}
		}
	}

}
