package model.id;

import model.state.GameState;
import model.world.tile.Tile;

public class TileId extends Id {

	public TileId(long id) {
		super(id);
	}

	@Override
	public Tile getFrom(GameState state) {
		return state.worldMap().chunk(id).tile(Tile.tileCoords(id));
	}

}
