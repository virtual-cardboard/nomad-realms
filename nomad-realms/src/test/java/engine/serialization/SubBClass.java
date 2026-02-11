package engine.serialization;

@Derializable
public class SubBClass extends SuperClass {
    public long subBField;

    public long getSubBField() {
        return subBField;
    }

    public void setSubBField(long subBField) {
        this.subBField = subBField;
    }
}
