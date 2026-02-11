package nomadrealms.context.game.actor.types.structure;

import nomadrealms.context.game.actor.types.structure.factory.StructureType;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.TREE;

public class TreeStructure extends Structure {

	public TreeStructure() {
		super("tree", "oak_tree", 1, 50);
	}

	@Override
	public StructureType structureType() {
		return TREE;
	}

}
