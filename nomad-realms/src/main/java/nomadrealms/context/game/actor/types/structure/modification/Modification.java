package nomadrealms.context.game.actor.types.structure.modification;

import nomadrealms.context.game.card.effect.Effect;

/**
 * Represents a way in which a structure modifies an effect.
 */
public abstract class Modification<T extends Effect> {

	public abstract Effect modify(T effect);


}
