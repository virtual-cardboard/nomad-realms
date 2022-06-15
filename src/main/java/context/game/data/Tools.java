package context.game.data;

import context.ResourcePack;
import debugui.ConsoleGui;

public final class Tools {

	public final ConsoleGui consoleGui;

	public Tools(ResourcePack resourcePack) {
		this.consoleGui = new ConsoleGui(resourcePack);
	}

	public void logMessage(String message) {
		consoleGui.log(message);
	}

	public void logMessage(String message, int colour) {
		consoleGui.log(message, colour);
	}

}
