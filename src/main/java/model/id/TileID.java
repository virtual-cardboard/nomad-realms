package model.id;

import model.state.GameState;
import model.world.Tile;

public class TileID extends ID {

	public TileID(long id) {
		super(id);
	}

	@Override
	public Tile getFrom(GameState state) {
		return state.worldMap().finalLayerChunk(id).tile(Tile.tileCoords(id));
	}

}
