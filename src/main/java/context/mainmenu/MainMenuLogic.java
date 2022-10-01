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
import engine.common.event.GameEvent;

public class MainMenuLogic extends GameLogic {

	private MainMenuData data;

	private boolean shouldTransition;
	private int ticksUntilTransition = 2;

	@Override
	protected void init() {
		data = (MainMenuData) context().data();
		addHandler(GameEvent.class, this.contextQueues()::pushEventFromLogic);
		addHandler(GameEvent.class, gameEvent -> {
			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAHHHHHHHHHHHH" + gameEvent);
		});
	}

	@Override
	public void update() {
		if (shouldTransition && ticksUntilTransition-- == 0) {
			transition();
		}
	}

	public void transition() {
		data.tools().logMessage("Transitioning to Sync Time", 0x29cf3aff);
		GameData data = new SyncTimeData(this.data.tools());
		GameInput input = new SyncTimeInput();
		GameLogic logic = new SyncTimeLogic();
		GameVisuals visuals = new SyncTimeVisuals();
		GameContext context = new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
		context().transition(context);
	}

	public boolean shouldTransition() {
		return shouldTransition;
	}

	public void setShouldTransition(boolean shouldTransition) {
		this.shouldTransition = shouldTransition;
	}

}
