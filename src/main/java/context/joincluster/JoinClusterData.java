package context.joincluster;

import context.data.GameData;
import engine.common.time.GameTime;

public class JoinClusterData extends GameData {

	private final GameTime gameTime;

	public JoinClusterData(GameTime time) {
		this.gameTime = time;
	}

	public GameTime gameTime() {
		return gameTime;
	}

}
