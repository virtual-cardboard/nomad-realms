package nomadrealms.context.game.actor.types.structure.modification;

import nomadrealms.context.game.card.effect.Effect;
import engine.serialization.Derializable;

/**
 * Represents a way in which a structure modifies an effect.
 */
@Derializable
public abstract class Modification<T extends Effect> {

	public abstract Effect modify(T effect);


}
