package model.id;

import model.state.GameState;
import model.world.Tile;

public class TileID extends ID {

	public TileID(long id) {
		super(id);
	}

	@Override
	public Tile getFrom(GameState state) {
		return state.worldMap().chunk(id).tile(Tile.tileCoords(id));
	}

}
