package main;

import context.GameContext;
import context.data.GameData;
import context.game.NomadsGameData;
import context.game.NomadsGameInput;
import context.game.NomadsGameLogic;
import context.game.NomadsGameVisuals;
import context.input.GameInput;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import engine.GameEngine;

public class NomadRealmsTestClient {

	public static void main(String[] args) {
		GameData data = new NomadsGameData();
		GameInput input = new NomadsGameInput();
		GameLogic logic = new NomadsGameLogic();
		GameVisuals visuals = new NomadsGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		new GameEngine("Nomad Realms Client", context)
				.enableLoading()
				.enableNetworking()
				.enableRendering()
				.enablePrintProgress()
				.run();
	}

}
