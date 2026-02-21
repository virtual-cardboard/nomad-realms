package nomadrealms.context.game.card.condition;

import nomadrealms.context.game.actor.Actor;
import nomadrealms.context.game.actor.types.structure.Structure;
import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.World;

public class StructureTypeCondition implements Condition {

	private final StructureType structureType;

	public StructureTypeCondition(StructureType structureType) {
		this.structureType = structureType;
	}

	@Override
	public boolean test(World world, Target target, Actor source) {
		return target instanceof Structure && ((Structure) target).structureType() == structureType;
	}

}
