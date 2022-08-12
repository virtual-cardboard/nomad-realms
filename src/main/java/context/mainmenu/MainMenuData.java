package context.mainmenu;

import context.data.GameData;
import context.game.data.Tools;

public class MainMenuData extends GameData {

	private Tools tools;

	@Override
	protected void init() {
		tools = new Tools(resourcePack());
	}

	public Tools tools() {
		return tools;
	}

}
