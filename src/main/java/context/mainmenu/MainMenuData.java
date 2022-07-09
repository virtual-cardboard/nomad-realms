package context.mainmenu;

import context.data.GameData;
import context.game.data.Tools;

public class MainMenuData extends GameData {

	private final Tools tools;

	public MainMenuData(Tools tools) {
		this.tools = tools;
	}

	public Tools tools() {
		return tools;
	}

}
