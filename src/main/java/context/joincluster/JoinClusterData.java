package context.joincluster;

import context.data.GameData;
import context.game.data.Tools;
import engine.common.time.GameTime;

public class JoinClusterData extends GameData {

	private final GameTime gameTime;
	private final Tools tools;

	public JoinClusterData(GameTime time, Tools tools) {
		this.gameTime = time;
		this.tools = tools;
	}

	public GameTime gameTime() {
		return gameTime;
	}

	public Tools tools() {
		return tools;
	}

}
