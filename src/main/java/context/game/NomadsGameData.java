package context.game;

import static model.map.tile.TileType.BLANK;

import context.data.GameData;
import model.map.TileMap;
import model.map.tile.TileType;

public class NomadsGameData extends GameData {

	private TileMap map;

	@Override
	protected void doInit() {
		TileType[][] types = {
				{ BLANK, BLANK },
				{ BLANK, BLANK },
		};
		map = new TileMap(types);
	}

	public TileMap map() {
		return map;
	}

}
