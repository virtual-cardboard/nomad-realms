package context.mainmenu;

import context.GameContext;
import context.audio.DefaultGameAudio;
import context.data.GameData;
import context.input.GameInput;
import context.join.JoinGameData;
import context.join.JoinGameInput;
import context.join.JoinGameLogic;
import context.join.JoinGameVisuals;
import context.logic.GameLogic;
import context.visuals.GameVisuals;

public class MainMenuInput extends GameInput {

	@Override
	protected void init() {
		MainMenuVisuals visuals = (MainMenuVisuals) context().visuals();
		addMousePressedFunction(new StartButtonMousePressedFunction(visuals, this));
	}

	public void transition() {
		GameData data = new JoinGameData();
		GameInput input = new JoinGameInput();
		GameLogic logic = new JoinGameLogic();
		GameVisuals visuals = new JoinGameVisuals();
		GameContext context = new GameContext(new DefaultGameAudio(), data, input, logic, visuals);
		context().transition(context);
	}

}
