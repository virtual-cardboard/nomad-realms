package context.game.visuals;

import app.NomadsSettings;
import context.GLContext;
import context.visuals.gui.RootGui;
import engine.common.math.Vector2f;

public class NomadsVisualsInfo {

	public GLContext glContext;
	public RootGui rootGui;
	public NomadsSettings settings;

	public NomadsVisualsInfo(GLContext glContext, RootGui rootGui, NomadsSettings settings) {
		this.glContext = glContext;
		this.rootGui = rootGui;
		this.settings = settings;
	}

	public Vector2f screenDim() {
		return screenDim();
	}

}
