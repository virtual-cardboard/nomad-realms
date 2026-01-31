package nomadrealms.context.game;

import nomadrealms.annotation.Derializable;

@Derializable
public class SomeClass {
    private String name;
    private int value;

    public SomeClass() {}

    public SomeClass(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
