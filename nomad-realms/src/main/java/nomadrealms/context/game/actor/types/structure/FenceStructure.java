package nomadrealms.context.game.actor.types.structure;

import nomadrealms.context.game.actor.types.structure.factory.StructureType;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.FENCE;

public class FenceStructure extends Structure {

	public FenceStructure() {
		super("fence", "fence", 1, 50);
	}

	@Override
	public StructureType structureType() {
		return FENCE;
	}

}
