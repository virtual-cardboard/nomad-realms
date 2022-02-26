package event.game.visualssync;

import model.actor.CardPlayer;
import model.id.ID;
import model.id.StructureID;
import model.id.TileID;

public class StructureSpawnedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private TileID tileID;
	private StructureID structureID;

	public StructureSpawnedSyncEvent(ID<? extends CardPlayer> playerID, TileID tileID, StructureID structureID) {
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
