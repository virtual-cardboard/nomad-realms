package engine.visuals.constraint.posdim;

import engine.visuals.constraint.Constraint;
import org.junit.jupiter.api.Test;

import static engine.visuals.constraint.posdim.AbsoluteConstraint.absolute;
import static engine.visuals.constraint.posdim.CustomSupplierConstraint.custom;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConstraintTest {

    @Test
    public void testAbsoluteConstraintMultiplyAbsolute() {
        // Test AbsoluteConstraint * AbsoluteConstraint
        AbsoluteConstraint c1 = new AbsoluteConstraint(5);
        AbsoluteConstraint c2 = new AbsoluteConstraint(10);
        Constraint result = c1.multiply(c2);
        assertEquals(50, result.get(), 0.001);
    }

    @Test
    public void testAbsoluteConstraintMultiplyCustomSupplier() {
        // Test AbsoluteConstraint * CustomSupplierConstraint
        AbsoluteConstraint c1 = new AbsoluteConstraint(5);
        CustomSupplierConstraint c2 = new CustomSupplierConstraint("test", () -> 10f);
        Constraint result = c1.multiply(c2);
        assertEquals(50, result.get(), 0.001);
    }

    @Test
    public void testCustomSupplierConstraintMultiplyAbsolute() {
        // Test CustomSupplierConstraint * AbsoluteConstraint
        CustomSupplierConstraint c1 = new CustomSupplierConstraint("test", () -> 5f);
        AbsoluteConstraint c2 = new AbsoluteConstraint(10);
        Constraint result = c1.multiply(c2);
        assertEquals(50, result.get(), 0.001);
    }

    @Test
    public void testAdditiveConstraintMultiply() {
        // Test (A + B) * C = A*C + B*C
        Constraint a = absolute(2);
        Constraint b = custom("b", () -> 3f);
        Constraint c = absolute(4);
        Constraint additive = new AdditiveConstraint(a, b);
        Constraint result = additive.multiply(c);
        assertEquals(20, result.get(), 0.001);
    }

    @Test
    public void testMultiplierConstraint() {
        // Test A * B * C
        Constraint a = absolute(2);
        Constraint b = custom("b", () -> 3f);
        Constraint c = absolute(4);
        Constraint multiplier = new MultiplierConstraint(a, b, c);
        assertEquals(24, multiplier.get(), 0.001);
    }

}
