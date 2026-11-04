package nomadrealms.render.particle.geometry;

import static engine.common.colour.Colour.rgba;
import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import engine.common.math.Vector2f;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.render.particle.Particle;
import nomadrealms.render.particle.spawner.ParticleFactory;
import org.junit.jupiter.api.Test;

public class CircleParticleTest {

	@Test
	public void testCircleParticleBoxConstructorAndClone() {
		ConstraintBox box = new ConstraintBox(absolute(10), absolute(20), absolute(30), absolute(30));
		int color = rgba(255, 200, 100, 150);
		CircleParticle particle = new CircleParticle(500, box, color);

		assertEquals(500, particle.lifetime());
		assertEquals(box, particle.box());
		assertEquals(color, particle.color());

		Particle clone = particle.clone();
		assertTrue(clone instanceof CircleParticle);
		CircleParticle circleClone = (CircleParticle) clone;
		assertEquals(particle.lifetime(), circleClone.lifetime());
		assertEquals(particle.color(), circleClone.color());
	}

	@Test
	public void testCircleParticleWorldPosConstructorAndClone() {
		Vector2f worldPos = new Vector2f(100f, 200f);
		float diameter = 40f;
		int color = rgba(240, 240, 255, 120);
		CircleParticle particle = new CircleParticle(600, worldPos, diameter, color);

		assertEquals(600, particle.lifetime());
		assertEquals(worldPos, particle.worldPos());
		assertEquals(diameter, particle.diameter());
		assertEquals(color, particle.color());
		assertNotNull(particle.box());

		Particle clone = particle.clone();
		assertTrue(clone instanceof CircleParticle);
		CircleParticle circleClone = (CircleParticle) clone;
		assertEquals(particle.lifetime(), circleClone.lifetime());
		assertEquals(particle.worldPos(), circleClone.worldPos());
		assertEquals(particle.diameter(), circleClone.diameter());
		assertEquals(particle.color(), circleClone.color());
	}

	@Test
	public void testParticleFactoryCreatesCircleParticle() {
		int color = rgba(100, 200, 255, 200);
		Particle particle = ParticleFactory.createParticle("circle", null, null, color);
		assertTrue(particle instanceof CircleParticle);
		assertEquals(color, ((CircleParticle) particle).color());
	}

}
