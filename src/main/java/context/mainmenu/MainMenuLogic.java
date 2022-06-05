package context.mainmenu;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.data.GameData;
import context.input.GameInput;
import context.logic.GameLogic;
import context.synctime.SyncTimeData;
import context.synctime.SyncTimeInput;
import context.synctime.SyncTimeLogic;
import context.synctime.SyncTimeVisuals;
import context.visuals.GameVisuals;

public class MainMenuLogic extends GameLogic {

	@Override
	public void update() {
		System.out.println("Transitioning to Sync Time");
		GameData data = new SyncTimeData();
		GameInput input = new SyncTimeInput();
		GameLogic logic = new SyncTimeLogic();
		GameVisuals visuals = new SyncTimeVisuals();
		GameContext context = new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
		context().transition(context);
	}

}
