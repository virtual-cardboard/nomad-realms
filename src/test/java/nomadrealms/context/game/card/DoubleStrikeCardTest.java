package nomadrealms.context.game.card;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

public class DoubleStrikeCardTest {

    private GameState gameState;
    private Nomad source;
    private Nomad target;

    @BeforeEach
    public void setUp() {
        gameState = new GameState("Test World", new LinkedList<>(), new nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy(123));
        Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
        Tile tile2 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 1, 0));
        source = new Nomad("Source", tile1);
        target = new Nomad("Target", tile2);
        gameState.world.addActor(source);
        gameState.world.addActor(target);
    }

    @Test
    public void testDoubleStrike_dealsDamageTwiceWithDelay() {
        target.health(10);

        List<Effect> effects = GameCard.DOUBLE_STRIKE.expression().effects(gameState.world, target, source);

        // Resolve initial effects
        // This should trigger the first DamageEffect(2) and queue the DelayedEffect
        effects.forEach(effect -> effect.resolve(gameState.world));

        // First damage should be dealt immediately
        assertEquals(8, target.health(), "Target should take 2 damage immediately");

        // Simulate game loop to process delay
        // Delay is 5 ticks
        for (int i = 0; i < 5; i++) {
            gameState.world.update(null);
            // During delay, no additional damage should occur yet
            assertEquals(8, target.health(), "Target should not take second damage during delay (tick " + i + ")");
        }

        // Next tick should trigger the delayed action
        gameState.world.update(null);

        // Second damage should be dealt
        assertEquals(6, target.health(), "Target should take another 2 damage after delay");
    }
}
