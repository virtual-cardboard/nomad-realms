package context.game.logic.asynchandler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.logicprocessing.SpawnSelfAsyncEvent;
import math.WorldPos;
import model.actor.Nomad;
import model.card.CardDashboard;
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

		CardDashboard cardDashboard = player.cardDashboard();
		data.deck().addTo(cardDashboard.deck(), data.currentState());
		cardDashboard.deck().shuffle(player.random(-1));
		for (int i = 0; i < 6; i++) {
			cardDashboard.hand().add(cardDashboard.deck().drawTop());
		}

		data.currentState().add(player);
		
		NomadID playerID = player.id();
		e.setPlayerID(playerID);
		data.setPlayerID(playerID);
		data.tools().logMessage("Spawned at " + spawnPos);
	}

}