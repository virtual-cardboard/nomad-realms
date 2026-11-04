package nomadrealms.context.game.actor.types.structure;

import nomadrealms.context.game.actor.types.structure.factory.StructureType;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.ROCK;
import engine.serialization.Derializable;

@Derializable
public class RockStructure extends Structure {

	public RockStructure() {
		super("rock", "rock_1", 1, 50);
	}

	@Override
	public StructureType structureType() {
		return ROCK;
	}

}
