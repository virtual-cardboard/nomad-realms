package nomadrealms.context.game.card.expression;

import static java.util.Collections.singletonList;
import static nomadrealms.context.game.actor.status.StatusEffect.BURNED;
import static nomadrealms.context.game.actor.status.StatusEffect.POISON;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedList;
import java.util.List;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.card.effect.ApplyStatusEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ThenExpressionTest {

    private GameState gameState;
    private Nomad source;

    @BeforeEach
    public void setUp() {
        gameState = new GameState("Test World", new LinkedList<>(),
                new OverworldGenerationStrategy(123));
        Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
        source = new Nomad("Source", tile1);
        gameState.world.addActor(source);
    }

    @Test
    public void testThenExpression_sequence() {
        CardExpression burn = (w, t, s) -> singletonList(new ApplyStatusEffect(s, BURNED, 1));
        CardExpression poison = (w, t, s) -> singletonList(new ApplyStatusEffect(s, POISON, 1));

        CardExpression sequence = burn.then(poison);

        // Resolve sequence
        List<Effect> effects = sequence.effects(gameState.world, null, source);
        // This should return one ThenEffect
        assertEquals(1, effects.size());

        effects.forEach(e -> e.resolve(gameState.world));

        // Initial state: nothing happened yet because ThenEffect queues an action
        assertEquals(0, source.status().count(BURNED));
        assertEquals(0, source.status().count(POISON));

        // Update 1: ThenAction runs. It runs `burn` (immediate). It queues `poison`.
        gameState.world.update(null);
        assertEquals(1, source.status().count(BURNED));
        assertEquals(0, source.status().count(POISON));

        // Update 2: ThenAction completes (post-delay check).
        gameState.world.update(null);
        assertEquals(1, source.status().count(BURNED));
        assertEquals(0, source.status().count(POISON));

        // Update 3: Poison action runs.
        gameState.world.update(null);
        assertEquals(1, source.status().count(BURNED));
        assertEquals(1, source.status().count(POISON));
    }

    @Test
    public void testThenExpression_nested() {
        CardExpression burn = (w, t, s) -> singletonList(new ApplyStatusEffect(s, BURNED, 1));

        // (burn -> burn) -> burn
        // Rotated to: burn -> (burn -> burn)
        CardExpression sequence = burn.then(burn).then(burn);

        sequence.effects(gameState.world, null, source).forEach(e -> e.resolve(gameState.world));

        // Update 1: ThenAction(burn, Then(burn, burn)) runs.
        // Runs burn immediately (Count 1). Queues Then(burn, burn).
        gameState.world.update(null);
        assertEquals(1, source.status().count(BURNED));

        // Update 2: ThenAction completes.
        gameState.world.update(null);
        assertEquals(1, source.status().count(BURNED));

        // Update 3: DelayedEffectAction(Then(B, C)) runs.
        // Adds ProcChain(ThenEffect). ThenEffect runs and Queues ThenAction(B, C).
        gameState.world.update(null);
        assertEquals(1, source.status().count(BURNED));

        // Update 4: DelayedEffectAction(Then(B, C)) completes.
        gameState.world.update(null);
        assertEquals(1, source.status().count(BURNED));

        // Update 5: ThenAction(B, C) runs.
        // Runs B immediately (Count 2). Queues C.
        gameState.world.update(null);
        assertEquals(2, source.status().count(BURNED));

        // Update 6: ThenAction(B, C) completes.
        gameState.world.update(null);
        assertEquals(2, source.status().count(BURNED));

        // Update 7: DelayedEffectAction(C) runs.
        // Runs C immediately (Count 3).
        gameState.world.update(null);
        assertEquals(3, source.status().count(BURNED));
    }
}
