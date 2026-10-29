package engine.serialization;

import java.util.List;
import java.util.Map;

@Derializable
public class PolymorphicListTestClass {
    private List<SuperClass> items;
    private Map<String, SuperClass> itemByName;

    public List<SuperClass> getItems() { return items; }
    public void setItems(List<SuperClass> items) { this.items = items; }

    public Map<String, SuperClass> getItemByName() { return itemByName; }
    public void setItemByName(Map<String, SuperClass> itemByName) { this.itemByName = itemByName; }
}
