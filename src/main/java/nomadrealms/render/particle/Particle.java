package nomadrealms.render.particle;

import static java.lang.Math.max;

import engine.visuals.constraint.Constraint;
import engine.visuals.constraint.box.ConstraintBox;
import nomadrealms.render.Renderable;

/**
 * A particle. Particles are small visual effects that have no gameplay impact.
 *
 * @author Lunkle
 */
public abstract class Particle implements Renderable {


	private final long lifetime;
	private final ConstraintBox box;

	/**
	 * The clockwise rotation of the particle in radians.
	 */
	private final Constraint rotation;

	public Particle(long lifetime, ConstraintBox box, Constraint rotation) {
		this.lifetime = lifetime;
		this.box = box;
		this.rotation = rotation;
	}

	public long lifetime() {
		return lifetime;
	}

	public ConstraintBox box() {
		return box;
	}

	public Constraint rotation() {
		return rotation;
	}

	/**
	 * Gets the big bounding box of the particle, which is the bounding box that contains the entire particle
	 * regardless of rotation. Is not exact.
	 *
	 * @return the big bounding box of the particle
	 */
	public ConstraintBox bigBoundingBox() {
		float max = max(box.w().get(), box.h().get());
		return box().translate(-max, -max).expand(2 * max, 2 * max);
	}

	public abstract Particle clone();
}
