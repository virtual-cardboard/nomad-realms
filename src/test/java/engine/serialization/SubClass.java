package engine.serialization;

@Derializable
public class SubClass extends SuperClass {
    public int subField;

    public int getSubField() {
        return subField;
    }

    public void setSubField(int subField) {
        this.subField = subField;
    }
}
