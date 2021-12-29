package context.game.visuals;

import app.NomadsSettings;
import common.math.Vector2f;
import context.GLContext;
import context.visuals.gui.RootGui;

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
