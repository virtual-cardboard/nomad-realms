package context.game.visuals;

import common.math.Vector2f;
import context.visuals.gui.RootGui;

public class GameCamera {

	private Vector2f pos = new Vector2f();

	public Vector2f pos() {
		return pos;
	}

	public void update(Vector2f target, RootGui rootGui) {
		pos.add(target.copy().sub(rootGui.dimensions().scale(0.5f)).sub(pos).scale(0.5f));
	}

}
