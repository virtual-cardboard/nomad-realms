package context.game.logic.asynchandler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.logicprocessing.SpawnSelfAsyncEvent;
import math.WorldPos;
import model.actor.Nomad;
import model.id.NomadID;

public class SpawnSelfAsyncEventHandler implements Consumer<SpawnSelfAsyncEvent> {

	private final NomadsGameData data;

	public SpawnSelfAsyncEventHandler(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public void accept(SpawnSelfAsyncEvent e) {
		WorldPos spawnPos = e.spawnPos();
		Nomad player = new Nomad();
		player.worldPos().set(spawnPos);

		data.deck().addTo(player.cardDashboard().deck(), data.currentState());
		data.currentState().add(player);

		NomadID playerID = player.id();
		e.setPlayerID(playerID);
		data.setPlayerID(playerID);
		data.logMessage("Spawned at " + spawnPos);
	}

}
