package nomadrealms.context.game.actor.types.structure;

import static nomadrealms.context.game.actor.types.structure.factory.StructureType.TOTEM_OF_PAIN;

import nomadrealms.context.game.actor.types.structure.factory.StructureType;
import nomadrealms.context.game.card.GameCard;
import nomadrealms.context.game.card.effect.AddCardToStackEffect;
import nomadrealms.context.game.card.effect.Effect;
import nomadrealms.context.game.world.World;

public class TotemOfPainStructure extends Structure {

	public TotemOfPainStructure() {
		super("Totem of Pain", "totem_of_pain", 1, 10);
	}

	@Override
	public Effect modify(World world, Effect effect) {
		if (effect instanceof AddCardToStackEffect) {
			AddCardToStackEffect addEffect = (AddCardToStackEffect) effect;
			if (addEffect.card() == GameCard.FEAR) {
				if (addEffect.target() != null && addEffect.target().tile() != null && this.tile() != null) {
					if (addEffect.target().tile().coord().distanceTo(this.tile().coord()) <= 6) {
						addEffect.count(addEffect.count() + 1);
					}
				}
			}
		}
		return effect;
	}

	@Override
	public StructureType structureType() {
		return TOTEM_OF_PAIN;
	}

}
