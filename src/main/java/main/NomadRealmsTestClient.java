package main;

import context.GameContext;
import context.data.GameData;
import context.input.DefaultGameInput;
import context.input.GameInput;
import context.loading.LoadingGameData;
import context.loading.LoadingGameLogic;
import context.loading.LoadingGameVisuals;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import engine.GameEngine;

public class NomadRealmsTestClient {

	public static void main(String[] args) {
		GameData data = new LoadingGameData();
		GameInput input = new DefaultGameInput();
		GameLogic logic = new LoadingGameLogic();
		GameVisuals visuals = new LoadingGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		new GameEngine("Nomad Realms Client", context)
				.enableLoading()
				.enableNetworking()
				.enableRendering()
				.enablePrintProgress()
				.run();
	}

}
