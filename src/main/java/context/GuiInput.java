package context;

import java.util.List;

import context.input.GameInput;
import context.visuals.GameVisuals;
import context.visuals.gui.Gui;
import context.visuals.gui.RootGui;
import context.visuals.gui.traits.HasMoveEffect;
import context.visuals.gui.traits.HasPressEffect;
import context.visuals.gui.traits.HasReleaseEffect;
import engine.common.math.Vector2i;

public abstract class GuiInput extends GameInput {

	protected void initGuiFunctions() {
		GameVisuals visuals = context().visuals();
		addMousePressedFunction(event -> getGui(HasPressEffect.class, cursor().pos(), visuals.rootGui()) != null,
				event -> ((HasPressEffect) getGui(HasPressEffect.class, cursor().pos(), visuals.rootGui())).doPressEffect(), true);
		addMouseReleasedFunction(event -> getGui(HasReleaseEffect.class, cursor().pos(), visuals.rootGui()) != null,
				event -> ((HasReleaseEffect) getGui(HasReleaseEffect.class, cursor().pos(), visuals.rootGui())).doReleaseEffect(), true);
		addMouseMovedFunction(event -> getGui(HasMoveEffect.class, cursor().pos(), visuals.rootGui()) != null,
				event -> ((HasMoveEffect) getGui(HasMoveEffect.class, cursor().pos(), visuals.rootGui())).doMoveEffect(), true);
	}

	private Gui getGui(Class<?> clazz, Vector2i coords, RootGui rootGui) {
		float width = rootGui.widthPx();
		float height = rootGui.heightPx();
		List<Gui> children = rootGui.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Gui g = doGetGui(clazz, coords, children.get(i), 0, 0, width, height);
			if (g != null) {
				return g;
			}
		}
		return null;
	}

	private Gui doGetGui(Class<?> clazz, Vector2i coords, Gui gui, float pX, float pY, float pW, float pH) {
		if (!gui.isEnabled()) {
			return null;
		}
		float x = gui.posX().get(pX, pX + pW);
		float y = gui.posY().get(pY, pY + pH);
		float w = gui.width().get(pX, pX + pW);
		float h = gui.height().get(pY, pY + pH);
		List<Gui> children = gui.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Gui g = doGetGui(clazz, coords, children.get(i), x, y, w, h);
			if (g != null) {
				return gui;
			}
		}
		if (clazz.isInstance(gui) && x <= coords.x && coords.x <= x + w && y <= coords.y && coords.y <= y + h) {
			return gui;
		}
		return null;
	}

}
