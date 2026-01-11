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


	private long lifetime;
	private ConstraintBox box;

	/**
	 * The clockwise rotation of the particle in radians.
	 */
	private Constraint rotation;

	public Particle(long lifetime, ConstraintBox box, Constraint rotation) {
		this.lifetime = lifetime;
		this.box = box;
		this.rotation = rotation;
	}

	public void lifetime(long lifetime) {
		this.lifetime = lifetime;
	}


	public long lifetime() {
		return lifetime;
	}

	public void box(ConstraintBox box) {
		this.box = box;
	}


	public ConstraintBox box() {
		return box;
	}

	public void rotation(Constraint rotation) {
		this.rotation = rotation;
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
