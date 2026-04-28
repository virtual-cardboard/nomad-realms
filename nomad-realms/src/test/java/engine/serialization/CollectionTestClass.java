package engine.serialization;

import java.util.List;
import java.util.Map;

@Derializable
public class CollectionTestClass {
    private List<String> stringList;
    private List<ShadowChild> childList;
    private Map<String, Integer> stringIntMap;
    private Map<String, ShadowChild> stringChildMap;

    public List<String> getStringList() { return stringList; }
    public void setStringList(List<String> stringList) { this.stringList = stringList; }

    public List<ShadowChild> getChildList() { return childList; }
    public void setChildList(List<ShadowChild> childList) { this.childList = childList; }

    public Map<String, Integer> getStringIntMap() { return stringIntMap; }
    public void setStringIntMap(Map<String, Integer> stringIntMap) { this.stringIntMap = stringIntMap; }

    public Map<String, ShadowChild> getStringChildMap() { return stringChildMap; }
    public void setStringChildMap(Map<String, ShadowChild> stringChildMap) { this.stringChildMap = stringChildMap; }
}
