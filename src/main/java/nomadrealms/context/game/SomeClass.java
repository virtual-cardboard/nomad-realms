package nomadrealms.context.game;

import engine.serialization.Derializable;

@Derializable
public class SomeClass {
    private String name;
    private int value;
    private long timestamp;
    private boolean active;
    private FieldClass nested;

    public SomeClass() {
    }

    public SomeClass(String name, int value, long timestamp, boolean active, FieldClass nested) {
        this.name = name;
        this.value = value;
        this.timestamp = timestamp;
        this.active = active;
        this.nested = nested;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public FieldClass getNested() {
        return nested;
    }

    public void setNested(FieldClass nested) {
        this.nested = nested;
    }
}
