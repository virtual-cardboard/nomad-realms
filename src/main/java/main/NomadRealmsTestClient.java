package main;

import context.GameContext;
import context.STUNLogic;
import context.data.DefaultGameData;
import context.data.GameData;
import context.input.DefaultGameInput;
import context.input.GameInput;
import context.logic.GameLogic;
import context.visuals.DefaultGameVisuals;
import context.visuals.GameVisuals;
import engine.GameEngine;

public class NomadRealmsTestClient {

	public static void main(String[] args) {
		GameData data = new DefaultGameData();
		GameInput input = new DefaultGameInput();
		GameLogic logic = new STUNLogic();
		GameVisuals visuals = new DefaultGameVisuals();
		GameContext context = new GameContext(data, input, logic, visuals);
		new GameEngine("Nomad Realms Test Client", context)
				.disableLoading()
				.disableNetworking()
				.disableRendering()
				.enablePrintProgress()
				.run();
	}

}
