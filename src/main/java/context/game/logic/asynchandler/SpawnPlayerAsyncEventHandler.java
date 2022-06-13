package context.game.logic.asynchandler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.logicprocessing.SpawnPlayerAsyncEvent;
import math.WorldPos;
import model.actor.Nomad;

public class SpawnPlayerAsyncEventHandler implements Consumer<SpawnPlayerAsyncEvent> {

	private final NomadsGameData data;

	public SpawnPlayerAsyncEventHandler(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public void accept(SpawnPlayerAsyncEvent spawnPlayerAsyncEvent) {
		WorldPos spawnPos = spawnPlayerAsyncEvent.spawnPos();
		Nomad newPlayer = new Nomad();
		newPlayer.worldPos().set(spawnPos);
		data.deck().addTo(newPlayer.cardDashboard().deck(), data.currentState());
		data.currentState().add(newPlayer);
		System.out.println("Spawned player successfully at " + spawnPos);
	}

}
