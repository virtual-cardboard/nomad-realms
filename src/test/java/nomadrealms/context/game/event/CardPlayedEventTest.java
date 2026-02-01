package nomadrealms.context.game.event;

import static nomadrealms.context.game.card.GameCard.INVINCIBILITY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.actor.types.cardplayer.Farmer;
import nomadrealms.context.game.card.WorldCard;
import nomadrealms.context.game.card.effect.SpawnParticlesEffect;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.generation.OverworldGenerationStrategy;
import nomadrealms.render.particle.ParticlePool;
import nomadrealms.render.particle.spawner.BasicParticleSpawner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CardPlayedEventTest {

    private GameState gameState;
    private Farmer source;
    private MockParticlePool particlePool;

    @BeforeEach
    public void setUp() {
        gameState = new GameState("Test World", new LinkedList<>(),
                new OverworldGenerationStrategy(123));
        particlePool = new MockParticlePool();
        gameState.particlePool(particlePool);

        Tile tile1 = gameState.world.getTile(new TileCoordinate(new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0));
        source = new Farmer("Source", tile1);
        gameState.world.addActor(source);
    }

    @Test
    public void testCardPlayed_spawnsParticles() {
        WorldCard card = new WorldCard(INVINCIBILITY);
        CardPlayedEvent event = new CardPlayedEvent(card, source, null);

        ProcChain procChain = event.procChain(gameState.world);

        while (!procChain.empty()) {
            procChain.update(gameState.world);
        }

        assertEquals(1, particlePool.capturedEffects.size(), "Should have spawned one particle effect");
        SpawnParticlesEffect effect = particlePool.capturedEffects.get(0);
        assertTrue(effect.spawner() instanceof BasicParticleSpawner);
        // We can't easily check private fields of BasicParticleSpawner, but we can verify it's the right class.
        // We could check reflection if needed, but existence is a good start.
    }

    static class MockParticlePool extends ParticlePool {
        List<SpawnParticlesEffect> capturedEffects = new ArrayList<>();

        public MockParticlePool() {
            super(null);
        }

        @Override
        public void addParticles(SpawnParticlesEffect effect) {
            capturedEffects.add(effect);
        }
    }
}
