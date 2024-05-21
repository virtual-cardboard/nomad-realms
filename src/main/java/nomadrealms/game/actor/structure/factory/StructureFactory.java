package nomadrealms.game.actor.structure.factory;

import nomadrealms.game.actor.structure.*;

public class StructureFactory {

    public static Structure createStructure(StructureType type) {
        switch (type) {
            case ROCK:
                return new RockStructure();
            case TREE:
                return new TreeStructure();
            case CHEST:
                return new ChestStructure();
            case FENCE:
                return new FenceStructure();
            case ELECTROSTATIC_ZAPPER:
                return new ElectrostaticZapperStructure();
            default:
                throw new RuntimeException("No structure case in StructureFactory for structure type " + type);
        }
    }

}
