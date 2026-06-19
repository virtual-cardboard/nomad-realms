package nomadrealms.context.game.world.architecture;

import static java.util.Arrays.asList;

import java.util.List;

import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.tile.SoilTile;

public class Village extends Architecture {

	public static final Village INSTANCE = new Village();

	private Village() {
	}

	@Override
	public void place(World world, TileCoordinate coord) {
		Tile tile = world.getTileIfLoaded(coord);
		if (tile == null) {
			return;
		}
		tile.clearActor();
		tile.actor(new VillageChief("Village Chief"));

		List<TileCoordinate> neighbors = asList(
				coord.ul(), coord.um(), coord.ur(),
				coord.dl(), coord.dm(), coord.dr()
		);
		for (TileCoordinate neighborCoord : neighbors) {
			Tile neighborTile = world.getTileIfLoaded(neighborCoord);
			if (neighborTile == null) {
				continue;
			}
			SoilTile soilTile = new SoilTile(neighborTile.chunk(), neighborCoord);
			neighborTile.copyStateTo(soilTile);
			neighborTile.clearActor();
			world.setTile(soilTile);
		}
	}

}
