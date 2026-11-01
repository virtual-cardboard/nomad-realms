package engine.serialization;

import java.util.ArrayList;
import java.util.List;

@Derializable
public class DerializerIdentityNode {

    public String name;

    public DerializerIdentityNode partner;

    public List<DerializerIdentityNode> children = new ArrayList<>();

    public DerializerIdentityNode() {
    }

    public DerializerIdentityNode(String name) {
        this.name = name;
    }

}
