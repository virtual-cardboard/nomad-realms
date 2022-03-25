package context.game.visuals.gui.dashboard;

import java.util.List;

import app.NomadsSettings;
import context.GLContext;
import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import model.state.GameState;

public class DiscardGui extends CardZoneGui {

	public DiscardGui(ResourcePack resourcePack, NomadsSettings settings) {
		setWidth(new PixelDimensionConstraint(settings.cardWidth()));
		setHeight(new PixelDimensionConstraint(settings.cardHeight()));
		setPosX(new PixelPositionConstraint(20));
		setPosY(new PixelPositionConstraint(20, height()));
	}

	@Override
	public void doRender(GLContext glContext, NomadsSettings s, GameState state, float x, float y, float width, float height) {
		boolean cardGuiOnTop = false;
		List<WorldCardGui> cardGuis = cardGuis();
		for (int i = cardGuis.size() - 2; i >= 0; i--) {
			if (cardGuiOnTop) {
				removeChild(i);
			} else if (cardGuis.get(i).inPlace()) {
				cardGuiOnTop = true;
			}
		}
	}

}
