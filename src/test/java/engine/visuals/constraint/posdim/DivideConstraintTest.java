package engine.visuals.constraint.posdim;

import engine.visuals.constraint.Constraint;
import org.junit.jupiter.api.Test;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DivideConstraintTest {

    @Test
    public void testDivideConstraint() {
        // Test AbsoluteConstraint / AbsoluteConstraint
        DivideConstraint constraint = new DivideConstraint(absolute(10), absolute(2));
        assertEquals(5, constraint.get(), 0.001);
    }

    @Test
    public void testDivideMethod() {
        // Test the fluent API a.divide(b)
        Constraint c1 = absolute(20);
        Constraint c2 = absolute(4);
        assertEquals(5, c1.divide(c2).get(), 0.001);
    }

    @Test
    public void testDivideByFloat() {
        // Test the fluent API a.divide(float)
        Constraint c1 = absolute(15);
        assertEquals(3, c1.divide(5f).get(), 0.001);
    }

    @Test
    public void testDivisionByZero() {
        // Test for division by zero, which should result in Infinity
        Constraint c1 = absolute(10);
        Constraint c2 = absolute(0);
        assertEquals(Float.POSITIVE_INFINITY, c1.divide(c2).get());
    }

    @Test
    public void testZeroDividedByZero() {
        // Test for 0/0, which should result in NaN
        Constraint c1 = absolute(0);
        Constraint c2 = absolute(0);
        assertTrue(Float.isNaN(c1.divide(c2).get()));
    }

    @Test
    public void testChainedDivision() {
        // Test (a / b) / c
        Constraint a = absolute(100);
        Constraint b = absolute(5);
        Constraint c = absolute(2);
        assertEquals(10, a.divide(b).divide(c).get(), 0.001);
    }

}
