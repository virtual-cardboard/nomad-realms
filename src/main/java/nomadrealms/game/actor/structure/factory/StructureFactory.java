package nomadrealms.game.actor.structure.factory;

import nomadrealms.game.actor.structure.RockStructure;
import nomadrealms.game.actor.structure.Structure;

public class StructureFactory {

    public static Structure createStructure(StructureType type) {
        switch (type) {
            case ROCK:
                return new RockStructure();
            default:
                throw new RuntimeException("No structure case in StructureFactory for structure type " + type);
        }
    }

}
