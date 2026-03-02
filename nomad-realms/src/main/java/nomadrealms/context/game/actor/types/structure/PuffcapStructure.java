package nomadrealms.context.game.actor.types.structure;

import nomadrealms.context.game.actor.types.structure.factory.StructureType;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.PUFFCAP;

public class PuffcapStructure extends Structure {

	public PuffcapStructure() {
		super("puffcap", "grass_2", 1, 10);
	}

	@Override
	public StructureType structureType() {
		return PUFFCAP;
	}

	@Override
	public boolean walkable() {
		return true;
	}

}
