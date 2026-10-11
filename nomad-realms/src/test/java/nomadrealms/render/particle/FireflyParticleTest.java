package nomadrealms.render.particle;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import engine.common.math.Vector2f;
import java.util.Random;
import nomadrealms.context.game.actor.types.cardplayer.CardPlayer;
import nomadrealms.context.game.actor.types.cardplayer.Nomad;
import nomadrealms.context.game.world.map.area.coordinate.ChunkCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.RegionCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.TileCoordinate;
import nomadrealms.context.game.world.map.area.coordinate.ZoneCoordinate;
import nomadrealms.context.game.world.map.tile.GrassTile;
import nomadrealms.render.particle.context.game.FireflyParticle;
import org.junit.jupiter.api.Test;

public class FireflyParticleTest {

	@Test
	public void testFireflyParticleLifetimeAndClone() {
		Vector2f pos = new Vector2f(100, 200);
		FireflyParticle particle = new FireflyParticle(pos);

		assertTrue(particle.lifetime() >= 1000 && particle.lifetime() <= 5000);
		assertNotNull(particle.box());

		Particle cloned = particle.clone();
		assertNotNull(cloned);
		assertTrue(cloned.lifetime() >= 1000 && cloned.lifetime() <= 5000);
	}

	@Test
	public void testRandomFireflyCountDistribution() {
		Random random = new Random(12345);
		int zeroCount = 0;
		int total = 10000;

		for (int i = 0; i < total; i++) {
			int count = GrassTile.getRandomFireflyCount(random);
			assertTrue(count >= 0 && count <= 3);
			if (count == 0) {
				zeroCount++;
			}
		}

		double zeroRatio = (double) zeroCount / total;
		assertTrue(zeroRatio > 0.50 && zeroRatio < 0.70, "Zero count ratio should be around 60%, got " + zeroRatio);
	}

	@Test
	public void testGrassTileOnStep() {
		TileCoordinate tileCoord = new TileCoordinate(
				new ChunkCoordinate(new ZoneCoordinate(new RegionCoordinate(0, 0), 0, 0), 0, 0), 0, 0
		);
		GrassTile grassTile = new GrassTile(null, tileCoord);

		float[] originalProbs = GrassTile.FIREFLY_PROBABILITIES;
		try {
			GrassTile.FIREFLY_PROBABILITIES = new float[]{0.0f, 0.0f, 0.0f, 1.0f};

			CardPlayer nomad = new Nomad("Test Nomad", null);
			ParticlePool pool = new ParticlePool(null, null, 100);
			nomad.particlePool(pool);

			grassTile.onStep(nomad);

		} finally {
			GrassTile.FIREFLY_PROBABILITIES = originalProbs;
		}
	}

}
