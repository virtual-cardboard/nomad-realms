package context.joincluster;

import context.data.GameData;
import context.game.data.DebugTools;
import engine.common.time.GameTime;

public class JoinClusterData extends GameData {

	private final GameTime gameTime;
	private final DebugTools tools;

	public JoinClusterData(GameTime time, DebugTools tools) {
		this.gameTime = time;
		this.tools = tools;
	}

	public GameTime gameTime() {
		return gameTime;
	}

	public DebugTools tools() {
		return tools;
	}

}
