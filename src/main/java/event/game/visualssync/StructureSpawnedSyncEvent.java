package event.game.visualssync;

public class StructureSpawnedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private long tileID;
	private long structureID;

	public StructureSpawnedSyncEvent(long playerID, long tileID, long structureID) {
		super(playerID);
		this.tileID = tileID;
		this.structureID = structureID;
	}

	public long tileID() {
		return tileID;
	}

	public long structureID() {
		return structureID;
	}

}
