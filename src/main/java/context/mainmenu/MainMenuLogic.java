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

	private MainMenuData data;

	@Override
	protected void init() {
		data = (MainMenuData) context().data();
	}

	@Override
	public void update() {
		data.tools().logMessage("Transitioning to Sync Time", 0x29cf3aff);
		GameData data = new SyncTimeData(this.data.tools());
		GameInput input = new SyncTimeInput();
		GameLogic logic = new SyncTimeLogic();
		GameVisuals visuals = new SyncTimeVisuals();
		GameContext context = new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
		context().transition(context);
	}

}
