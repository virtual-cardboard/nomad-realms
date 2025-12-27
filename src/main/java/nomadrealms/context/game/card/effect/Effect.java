package nomadrealms.context.game.card.effect;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.card.query.Query;

/**
 * An effect that will cause some change in the {@link GameState} when resolved. The effect should not need any parameters
 * when being resolved, as all necessary information should be encapsulated within the effect itself.
 * <p>
 * Effects should not store any {@link Query Queries}. Queries should be resolved beforehand and found objects should
 * be passed in when constructing the effect.
 *
 * @author Lunkle
 */
public interface Effect {

	public void resolve();

}
