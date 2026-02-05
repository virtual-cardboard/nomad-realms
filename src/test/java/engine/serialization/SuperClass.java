package engine.serialization;

@Derializable
public abstract class SuperClass {
    public int superField;

    public int getSuperField() {
        return superField;
    }

    public void setSuperField(int superField) {
        this.superField = superField;
    }
}
