package nomadrealms.context.game.actor.types.structure.factory;

import nomadrealms.context.game.actor.types.structure.ChestStructure;
import nomadrealms.context.game.actor.types.structure.ElectrostaticZapperStructure;
import nomadrealms.context.game.actor.types.structure.FenceStructure;
import nomadrealms.context.game.actor.types.structure.RockStructure;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.actor.types.structure.TreeStructure;

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
