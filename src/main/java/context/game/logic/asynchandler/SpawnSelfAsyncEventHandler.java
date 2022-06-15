package context.game.logic.asynchandler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.logicprocessing.SpawnSelfAsyncEvent;
import math.WorldPos;
import model.actor.Nomad;

public class SpawnSelfAsyncEventHandler implements Consumer<SpawnSelfAsyncEvent> {

	private final NomadsGameData data;

	public SpawnSelfAsyncEventHandler(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public void accept(SpawnSelfAsyncEvent spawnPlayerAsyncEvent) {
		WorldPos spawnPos = spawnPlayerAsyncEvent.spawnPos();
		Nomad player = new Nomad();
		player.worldPos().set(spawnPos);
		data.deck().addTo(player.cardDashboard().deck(), data.currentState());
		data.currentState().add(player);
		System.out.println("Spawned self successfully at " + spawnPos);
	}

}
