package context.mainmenu;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.data.DefaultGameData;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.synctime.joincluster.SyncTimeInput;
import context.synctime.joincluster.SyncTimeLogic;
import context.synctime.joincluster.SyncTimeVisuals;
import context.visuals.GameVisuals;

public class MainMenuLogic extends GameLogic {

	@Override
	public void update() {
		System.out.println("Transitioning to Sync Time");
		GameData data = new DefaultGameData();
		GameInput input = new SyncTimeInput();
		GameLogic logic = new SyncTimeLogic();
		GameVisuals visuals = new SyncTimeVisuals();
		GameContext context = new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
		context().transition(context);
	}

}
