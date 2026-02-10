package nomadrealms.context.game.actor.types.structure;

import nomadrealms.context.game.actor.types.structure.factory.StructureType;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.CHEST;

public class ChestStructure extends Structure {

	public ChestStructure() {
		super("chest", "chest", 1, 50);
	}

	@Override
	public StructureType structureType() {
		return CHEST;
	}

}
