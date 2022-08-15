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
		// TODO replace personal id generator with joining player's id generator
		player.setId(data.generators().genId());
		player.worldPos().set(spawnPos);

		CardDashboard cardDashboard = player.cardDashboard();
		// TODO: Replace personal id generator with the joining player's id generator
		data.deck().addTo(cardDashboard.deck(), data.nextState(), data.generators().personalIdGenerator());
		cardDashboard.deck().shuffle(player.getRandom(-1));
		for (int i = 0; i < 6; i++) {
			cardDashboard.hand().add(cardDashboard.deck().drawTop());
		}

		data.nextState().add(player);
		data.tools().logMessage("Spawned new player at " + spawnPos);
	}

}
