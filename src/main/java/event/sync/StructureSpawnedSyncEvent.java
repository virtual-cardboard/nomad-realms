package event.sync;

import model.id.CardPlayerId;
import model.id.StructureId;
import model.id.TileId;

public class StructureSpawnedSyncEvent extends NomadRealmsSyncEvent {

	private TileId tileID;
	private StructureId structureID;

	public StructureSpawnedSyncEvent(CardPlayerId playerID, TileId tileID, StructureId structureID) {
		super(playerID);
		this.tileID = tileID;
		this.structureID = structureID;
	}

	public TileId tileID() {
		return tileID;
	}

	public StructureId structureID() {
		return structureID;
	}

}
