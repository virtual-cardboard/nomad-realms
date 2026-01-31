package nomadrealms.context.game;

import nomadrealms.annotation.Derializable;

@Derializable
public class NestedClass {
    private int id;
    private String description;

    public NestedClass() {}

    public NestedClass(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
