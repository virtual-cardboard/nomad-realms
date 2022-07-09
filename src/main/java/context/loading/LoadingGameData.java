package context.loading;

import context.data.GameData;
import context.game.data.Tools;
import context.visuals.gui.constraint.dimension.PixelDimensionConstraint;
import context.visuals.gui.constraint.position.PixelPositionConstraint;

public class LoadingGameData extends GameData {

	private Tools tools;

	public Tools tools() {
		return tools;
	}

	public void initTools() {
		tools = new Tools(resourcePack());
		tools.consoleGui.setPadding(20);
		tools.consoleGui.setPosY(new PixelPositionConstraint(0));
		tools.consoleGui.setWidth(new PixelDimensionConstraint(800));
		tools.consoleGui.setHeight(new PixelDimensionConstraint(-300));

		tools.logMessage("This is the console gui! Press 'T' to toggle it.", 0xfcba03ff);
	}

}
