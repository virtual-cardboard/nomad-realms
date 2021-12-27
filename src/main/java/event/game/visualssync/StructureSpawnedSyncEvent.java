package event.game.visualssync;

import model.actor.CardPlayer;
import model.actor.Structure;
import model.tile.Tile;

public class StructureSpawnedSyncEvent extends NomadRealmsVisualsSyncEvent {

	private Tile tile;
	private Structure structure;

	public StructureSpawnedSyncEvent(CardPlayer player, Tile tile, Structure structure) {
		super(player);
		this.tile = tile;
		this.structure = structure;
	}

	public Tile tile() {
		return tile;
	}

	public Structure structure() {
		return structure;
	}

}
