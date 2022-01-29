package context.game.visuals;

import app.NomadsSettings;
import common.math.Vector2f;
import common.math.Vector2i;
import context.visuals.gui.RootGui;
import math.WorldPos;

public class GameCamera {

	public static final int RENDER_RADIUS = 1;

	private Vector2i chunkPos = new Vector2i(-1, 0);
	private Vector2f pos = new Vector2f(0, 0);

	public void update(NomadsSettings s, WorldPos target, RootGui rootGui) {
		Vector2i chunkDiff = target.chunkPos().sub(chunkPos);

		if (chunkDiff.lengthSquared() >= 16) {
			chunkPos = chunkPos.add(chunkDiff.scale(4 / chunkDiff.lengthSquared()));
		} else {
			Vector2f posDiff = chunkDiff.toVec2f().multiply(s.chunkWidth(), s.chunkHeight());
			chunkPos = target.chunkPos();
			pos = pos.sub(posDiff);
			Vector2f screenPos = target.screenPos(this, s);
			pos = pos.add(screenPos.sub(rootGui.dimensions().scale(0.5f)).scale(0.3f));
		}
	}

	public Vector2i chunkPos() {
		return chunkPos;
	}

	public Vector2f pos() {
		return pos;
	}

	public void setPos(Vector2f pos) {
		this.pos = pos;
	}

}
