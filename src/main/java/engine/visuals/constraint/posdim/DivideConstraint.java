package engine.visuals.constraint.posdim;

import engine.visuals.constraint.Constraint;

public class DivideConstraint implements Constraint {

    private final Constraint numerator;
    private final Constraint denominator;

    public DivideConstraint(Constraint numerator, Constraint denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    @Override
    public float get() {
        return numerator.get() / denominator.get();
    }

}
