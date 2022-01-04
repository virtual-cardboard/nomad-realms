package event.game.visualssync;

public class StructureSpawnedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long structureID;

	public StructureSpawnedSyncEvent(long playerID, long structureID) {
		super(playerID);
		this.structureID = structureID;
	}

	public long structureID() {
		return structureID;
	}

}
