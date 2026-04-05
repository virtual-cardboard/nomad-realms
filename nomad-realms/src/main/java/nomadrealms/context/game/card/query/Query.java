package nomadrealms.context.game.card.query;

import java.util.List;

import nomadrealms.event.game.effect.EffectContext;

/**
 * A query expression that can be used by card expressions and intents to find objects in the game world.
 *
 * @param <T> The type of object being queried
 * @author Lunkle
 */
public interface Query<T> {

	public List<T> find(EffectContext context);

}
