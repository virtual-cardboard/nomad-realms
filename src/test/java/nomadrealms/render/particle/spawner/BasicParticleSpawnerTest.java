package nomadrealms.render.particle.spawner;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import engine.visuals.constraint.box.ConstraintPair;
import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.card.query.Query;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.context.game.world.map.tile.factory.TileType;
import nomadrealms.render.RenderingEnvironment;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.ParticleParameters;
import nomadrealms.render.ui.Camera;

public class BasicParticleSpawnerTest {

	@Test
	public void testSpawnImmediate() {
		BasicParticleSpawner spawner = new BasicParticleSpawner(new MockQuery(), "fire_directional");
		spawner.particleCount(5);

		ParticleParameters params = new ParticleParameters()
				.renderingEnvironment(new MockRenderingEnvironment());

		List<Particle> particles = spawner.spawnParticles(params);

		assertEquals(5, particles.size());
	}

	@Test
	public void testDelay() throws InterruptedException {
		BasicParticleSpawner original = new BasicParticleSpawner(new MockQuery(), "fire_directional");
		original.particleCount(3).delay(100);

		// Must use copy() to initialize state properly as the app would
		BasicParticleSpawner spawner = original.copy();

		ParticleParameters params = new ParticleParameters()
				.renderingEnvironment(new MockRenderingEnvironment());

		// Frame 1: Immediate call. lastSpawnTime initialized to 0.
		// now - lastSpawnTime >= 100 is true (assuming epoch > 100).
		// So first particle spawns immediately.
		List<Particle> particles = spawner.spawnParticles(params);
		assertEquals(1, particles.size(), "Should spawn 1st particle immediately");
		assertFalse(spawner.isComplete());

		// Frame 2: Wait < 100ms (e.g., immediate next frame)
		particles = spawner.spawnParticles(params);
		assertEquals(0, particles.size(), "Should wait for delay after 1st spawn");

		// Frame 3: Wait > 100ms
		Thread.sleep(120);
		particles = spawner.spawnParticles(params);
		assertEquals(1, particles.size(), "Should spawn 2nd particle");
		assertFalse(spawner.isComplete());

		// Frame 4: Wait > 100ms
		Thread.sleep(120);
		particles = spawner.spawnParticles(params);
		assertEquals(1, particles.size(), "Should spawn 3rd particle");
		assertTrue(spawner.isComplete());

		// Frame 5: Post completion
		Thread.sleep(120);
		particles = spawner.spawnParticles(params);
		assertEquals(0, particles.size(), "Should not spawn anymore");
	}

	// Mocks and Stubs
	static class MockQuery implements Query<Target> {
		@Override
		public List<Target> find(World world, Actor source, Target target) {
			return Collections.singletonList(new MockTarget());
		}
	}

	static class MockTarget implements Target {
		@Override
		public Tile tile() {
			return new MockTile();
		}
	}

	static class MockTile extends Tile {
		public MockTile() {
			super();
		}

		@Override
		public ConstraintPair getScreenPosition(RenderingEnvironment re) {
			return new ConstraintPair(absolute(0), absolute(0));
		}

		@Override
		public TileType type() {
			return TileType.GRASS;
		}
	}

	static class MockRenderingEnvironment extends RenderingEnvironment {
		public MockRenderingEnvironment() {
			super();
			// We leave glContext as null.
			this.camera = new Camera(0, 0);
		}
	}
}
