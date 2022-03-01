package event.game.sync;

import model.id.CardPlayerID;
import model.id.StructureID;
import model.id.TileID;

public class StructureSpawnedSyncEvent extends NomadRealmsSyncEvent {

	private TileID tileID;
	private StructureID structureID;

	public StructureSpawnedSyncEvent(CardPlayerID playerID, TileID tileID, StructureID structureID) {
		super(playerID);
		this.tileID = tileID;
		this.structureID = structureID;
	}

	public TileID tileID() {
		return tileID;
	}

	public StructureID structureID() {
		return structureID;
	}

}
