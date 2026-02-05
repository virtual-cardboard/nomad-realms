package engine.serialization;

@Derializable
public class SubAClass extends SuperClass {
    public int subAField;

    public int getSubAField() {
        return subAField;
    }

    public void setSubAField(int subAField) {
        this.subAField = subAField;
    }
}
