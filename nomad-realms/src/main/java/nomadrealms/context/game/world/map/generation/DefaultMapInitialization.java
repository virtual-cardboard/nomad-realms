package nomadrealms.context.game.world.map.generation;

import static nomadrealms.context.game.item.Item.OAK_LOG;
import static nomadrealms.context.game.item.Item.WHEAT_SEED;

import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.actor.types.cardplayer.FeralMonkey;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.actor.types.cardplayer.VillageLumberjack;
import nomadrealms.context.game.actor.types.cardplayer.Wolf;
import nomadrealms.context.game.item.WorldItem;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;

public class DefaultMapInitialization implements MapInitialization {

	@Override
	public void initialize(World world) {
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

		// Test actor for village chief type

		VillageChief villageChief = new VillageChief("Chief guy");
		villageChief.tile(world.getTile(new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 15, 15)));
		world.addActor(villageChief, true);

		// Test actor for VillageLumberjack type
		world.addActor(new VillageLumberjack("Lumberjack guy", world.getTile(new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 10, 10))), true);
	}

}
