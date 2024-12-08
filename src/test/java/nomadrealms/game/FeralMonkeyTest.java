package nomadrealms.game;

import nomadrealms.game.actor.cardplayer.Farmer;
import nomadrealms.game.actor.cardplayer.FeralMonkey;
import nomadrealms.game.world.World;
import nomadrealms.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.game.world.map.area.coordinate.ZoneCoordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FeralMonkeyTest {

    @Test
    public void testFeralMonkeyKillsFarmerWithin400Ticks() {
        GameState gameState = new GameState();
        World world = new World(gameState);

        Farmer farmer = new Farmer("Test Farmer", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 1)));
        FeralMonkey feralMonkey = new FeralMonkey("Test Feral Monkey", world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 6, 6)));

        world.actors.add(farmer);
        world.actors.add(feralMonkey);

        int ticks = 0;
        int walkTicks = 0;
        int attackTicks = 0;
        boolean isWalking = true;

        for (int i = 0; i < 400; i++) {
            world.update(null);
            ticks++;
            if (farmer.health() <= 0) {
                break;
            }
            if (isWalking && feralMonkey.tile().coord().distanceTo(farmer.tile().coord()) <= 1) {
                isWalking = false;
            }
            if (isWalking) {
                walkTicks++;
            } else {
                attackTicks++;
            }
        }
        System.out.println("");
        System.out.println("Ticks taken for feral monkey to kill farmer: " + ticks);
        System.out.println("Ticks taken to walk: " + walkTicks);
        System.out.println("Ticks taken to attack: " + attackTicks);
        assertTrue(farmer.health() <= 0, "Feral monkey did not kill the farmer within 400 ticks");
    }
}
