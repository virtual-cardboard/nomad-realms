package context.mainmenu;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.data.DefaultGameData;
import context.data.GameData;
import context.input.GameInput;
import context.joincluster.JoinClusterInput;
import context.joincluster.JoinClusterLogic;
import context.joincluster.JoinClusterVisuals;
import context.logic.GameLogic;
import context.visuals.GameVisuals;

public class MainMenuLogic extends GameLogic {

	@Override
	public void update() {
		GameData data = new DefaultGameData();
		GameInput input = new JoinClusterInput();
		GameLogic logic = new JoinClusterLogic();
		GameVisuals visuals = new JoinClusterVisuals();
		GameContext context = new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
		context().transition(context);
	}

}
