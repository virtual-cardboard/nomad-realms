package engine.serialization;

@Derializable
public class ShadowChild extends ShadowParent {
    private int val;

    public void setChildVal(int val) {
        this.val = val;
    }

    public int getChildVal() {
        return val;
    }
}
