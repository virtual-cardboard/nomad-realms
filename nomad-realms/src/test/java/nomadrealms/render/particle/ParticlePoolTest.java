package nomadrealms.render.particle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nomadrealms.render.RenderingEnvironment;
import org.junit.jupiter.api.Test;

public class ParticlePoolTest {

	private static class DummyParticle extends Particle {
		public boolean rendered = false;

		public DummyParticle(long lifetime) {
			super(lifetime, null, null);
		}

		@Override
		public void render(RenderingEnvironment re) {
			rendered = true;
		}

		@Override
		public Particle clone() {
			return new DummyParticle(lifetime());
		}
	}

	@Test
	public void testAddAndRender() {
		ParticlePool pool = new ParticlePool(null, null, 10);
		DummyParticle particle1 = new DummyParticle(10000L);
		DummyParticle particle2 = new DummyParticle(0L); // expires immediately

		pool.addParticle(particle1);
		pool.addParticle(particle2);

		assertEquals(2, pool.pool().size());

		pool.render(null);

		// particle2 should expire during render pass
		assertEquals(1, pool.pool().size());
		assertEquals(particle1, pool.pool().get(0));
	}

	@Test
	public void testNullParticlePool() {
		NullParticlePool nullPool = new NullParticlePool();
		DummyParticle particle = new DummyParticle(5000L);
		nullPool.addParticle(particle);
		assertEquals(0, nullPool.pool().capacity());
		assertEquals(0, nullPool.pool().size());
	}

}
