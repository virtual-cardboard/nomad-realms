package engine.visuals.constraint.posdim;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import engine.visuals.constraint.Constraint;

/**
 *
 */
public class MultiplierConstraint implements Constraint {

	private final List<Constraint> constraints = new ArrayList<>();

	private MultiplierConstraint(List<Constraint> constraints) {
		Queue<Constraint> toProcess = new LinkedList<>(constraints);
		float absolutes = 1;
		while (!toProcess.isEmpty()) {
			Constraint c = toProcess.poll();
			if (c instanceof AbsoluteConstraint) {
				AbsoluteConstraint constraint = (AbsoluteConstraint) c;
				absolutes *= constraint.get();
			} else if (c instanceof MultiplierConstraint) {
				MultiplierConstraint constraint = (MultiplierConstraint) c;
				toProcess.addAll(constraint.constraints);
			} else {
				this.constraints.add(c);
			}
		}
		if (absolutes != 1) {
			this.constraints.add(absolute(absolutes));
		}
	}

	/**
	 * Constructs a new {@link MultiplierConstraint} from a list of {@link Constraint}s.
	 *
	 * @param constraints the constraints to multiply together
	 */
	public MultiplierConstraint(Constraint... constraints) {
		this(asList(constraints));
		if (constraints.length == 0) {
			throw new IllegalArgumentException("Must have at least one constraint");
		}
	}

	@Override
	public float get() {
		float product = 1;
		for (Constraint c : constraints) {
			product *= c.get();
		}
		return product;
	}

	@Override
	public Constraint neg() {
		List<Constraint> constraints = new ArrayList<>(this.constraints);
		constraints.add(absolute(-1));
		return new MultiplierConstraint(constraints).flatten();
	}

	@Override
	public Constraint flatten() {
		if (this.constraints.size() == 1) {
			return this.constraints.get(0).flatten();
		}
		return this;
	}

	@Override
	public int size() {
		int size = 1;
		for (Constraint c : constraints) {
			size += c.size();
		}
		return size;
	}

	@Override
	public Constraint doMultiply(AbsoluteConstraint c) {
		float value = c.get();
		if (value == 0) {
			return absolute(0);
		} else if (value == 1) {
			return this;
		} else if (value == -1) {
			return this.neg();
		} else {
			List<Constraint> newConstraints = new ArrayList<>(this.constraints);
			newConstraints.add(c);
			return new MultiplierConstraint(newConstraints).flatten();
		}
	}

}