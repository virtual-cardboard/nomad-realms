package engine.serialization;

@Derializable
public class ArrayTestClass {
    private int[][] intGrid;
    private String[] names;
    private ShadowChild[][] children;

    public int[][] getIntGrid() { return intGrid; }
    public void setIntGrid(int[][] intGrid) { this.intGrid = intGrid; }

    public String[] getNames() { return names; }
    public void setNames(String[] names) { this.names = names; }

    public ShadowChild[][] getChildren() { return children; }
    public void setChildren(ShadowChild[][] children) { this.children = children; }
}
