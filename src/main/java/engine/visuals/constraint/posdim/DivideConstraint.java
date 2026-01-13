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

    @Override
    public int size() {
        return 1 + numerator.size() + denominator.size();
    }

    @Override
    public Constraint flatten() {
        if (denominator instanceof AbsoluteConstraint) {
            if (((AbsoluteConstraint) denominator).get() == 1) {
                return numerator;
            }
        }
        return this;
    }

}
