package context.game.data;

import context.ResourcePack;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;
import debugui.ConsoleGui;

public final class DebugTools {

	public final ConsoleGui consoleGui;

	public DebugTools(ResourcePack resourcePack) {
		consoleGui = new ConsoleGui(resourcePack, 0);
		init();
	}

	private void init() {
		consoleGui.setPadding(20);
		consoleGui.setPosY(new PixelPositionConstraint(0));
		consoleGui.setWidth(new PixelDimensionConstraint(800));
		consoleGui.setHeight(new PixelDimensionConstraint(-300));

	}

	public void logMessage(Object object) {
		consoleGui.log(object.toString());
	}

	public void logMessage(String message) {
		consoleGui.log(message);
	}

	public void logMessage(String message, int colour) {
		consoleGui.log(message, colour);
	}

}
