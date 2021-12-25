package context.game.visuals.handler;

import java.util.function.Consumer;

import context.ResourcePack;
import event.game.visualssync.CreatureSpawnedSyncEvent;

public class CreatureSpawnedSyncEventHandler implements Consumer<CreatureSpawnedSyncEvent> {

	private ResourcePack resourcePack;

	public CreatureSpawnedSyncEventHandler(ResourcePack resourcePack) {
		this.resourcePack = resourcePack;
	}

	@Override
	public void accept(CreatureSpawnedSyncEvent t) {
		t.creature().displayer().init(resourcePack);
	}

}
