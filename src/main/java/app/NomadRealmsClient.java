package app;

import context.GameContext;
import context.audio.GameAudio;
import context.data.GameData;
import context.input.DefaultGameInput;
import context.input.GameInput;
import context.loading.LoadingGameAudio;
import context.loading.LoadingGameData;
import context.loading.LoadingGameLogic;
import context.loading.LoadingGameVisuals;
import context.logic.GameLogic;
import context.visuals.GameVisuals;
import engine.GameEngine;

public class NomadRealmsClient {

	public static final boolean DEBUG = false;
	public static final boolean SKIP_NETWORKING = true;
	public static final long TICK_RATE = 10;
	public static final long TICK_TIME = 1000 / TICK_RATE;

	public static void main(String[] args) {
		GameAudio audio = new LoadingGameAudio();
		GameData data = new LoadingGameData();
		GameInput input = new DefaultGameInput();
		GameLogic logic = new LoadingGameLogic();
		GameVisuals visuals = new LoadingGameVisuals();

		GameContext context = new GameContext(audio, data, input, logic, visuals);
		new GameEngine("Nomad Realms", context)
				.enableLoading()
				.enableNetworking(44000)
				.enableRendering()
				.setTickRate(TICK_RATE)
//				.enablePrintProgress()
				.run();
//		Loading
//		Main Menu
//		Verification - not implemented
//      Sync time
//		Join Cluster
//		Peer Connect
//      Game
	}

}
