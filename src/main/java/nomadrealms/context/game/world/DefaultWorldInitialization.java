package nomadrealms.context.game.world;

import static nomadrealms.context.game.item.Item.OAK_LOG;
import static nomadrealms.context.game.item.Item.WHEAT_SEED;

import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.actor.types.cardplayer.FeralMonkey;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.cardplayer.Wolf;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;

public class DefaultWorldInitialization implements WorldInitialization {

	@Override
	public void apply(World world) {
		world.nomad = new Nomad("Donny",
				world.getTile(new TileCoordinate(
						new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0),
						0, 8)));
		world.nomad.inventory().add(new WorldItem(OAK_LOG));
		world.nomad.inventory().add(new WorldItem(WHEAT_SEED));
		Farmer farmer = new Farmer("Fred",
				world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0),
						0, 1), 0, 0)));
		world.addActor(world.nomad, true);
		world.addActor(farmer, true);
		world.addActor(new FeralMonkey("bob", world.getTile(new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 6, 6))), true);
		world.addActor(new Wolf("ghost", world.getTile(new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 2, 2))), true);
	}

}
