package context.game.logic.asynchandler;

import java.util.function.Consumer;

import context.game.NomadsGameData;
import event.logicprocessing.SpawnPlayerAsyncEvent;
import math.WorldPos;
import model.actor.Nomad;
import model.card.CardDashboard;

public class SpawnPlayerAsyncEventHandler implements Consumer<SpawnPlayerAsyncEvent> {

	private final NomadsGameData data;

	public SpawnPlayerAsyncEventHandler(NomadsGameData data) {
		this.data = data;
	}

	@Override
	public void accept(SpawnPlayerAsyncEvent e) {
		WorldPos spawnPos = e.spawnPos();
		Nomad player = new Nomad();
		player.worldPos().set(spawnPos);
		data.deck().addTo(player.cardDashboard().deck(), data.currentState());

		CardDashboard cardDashboard = player.cardDashboard();
		data.deck().addTo(cardDashboard.deck(), data.currentState());
		cardDashboard.deck().shuffle(player.random(-1));
		for (int i = 0; i < 6; i++) {
			cardDashboard.hand().add(cardDashboard.deck().drawTop());
		}

		data.currentState().add(player);
		data.tools().logMessage("Spawned new player at " + spawnPos);
	}

}
