package nomadrealms.context.game.world.map.generation;

import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.actor.types.cardplayer.FeralMonkey;
import nomadrealms.context.game.actor.types.cardplayer.VillageChief;
import nomadrealms.context.game.actor.types.cardplayer.VillageLumberjack;
import nomadrealms.context.game.actor.types.cardplayer.Wolf;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;

public class TerrainSandboxMapInitialization implements MapInitialization {

	@Override
	public void initialize(World world) {
		Farmer farmer = new Farmer("Fred",
				world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0),
						8, 9), 0, 0)));
		world.addActor(farmer, true);
		world.addActor(new FeralMonkey("bob", world.getTile(new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 8, 8), 6, 6))), true);
		world.addActor(new Wolf("ghost", world.getTile(new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 8, 8), 2, 2))), true);

		VillageChief villageChief = new VillageChief("Chief guy");
		villageChief.tile(world.getTile(new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 8, 8), 15, 15)));
		world.addActor(villageChief, true);

		world.addActor(new VillageLumberjack("Lumberjack guy", world.getTile(new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 8, 8), 10, 10))), true);
	}

}
