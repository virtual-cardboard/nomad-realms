package context.mainmenu;

import context.data.GameData;
import context.game.data.DebugTools;

public class MainMenuData extends GameData {

	private DebugTools tools;

	@Override
	protected void init() {
		tools = new DebugTools(resourcePack());
	}

	public DebugTools tools() {
		return tools;
	}

}
