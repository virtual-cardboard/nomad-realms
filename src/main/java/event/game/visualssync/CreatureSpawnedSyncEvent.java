package event.game.visualssync;

import model.actor.Creature;

public class CreatureSpawnedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private Creature creature;

	public CreatureSpawnedSyncEvent(Creature creature) {
		this.creature = creature;
	}

	public Creature creature() {
		return creature;
	}

}
