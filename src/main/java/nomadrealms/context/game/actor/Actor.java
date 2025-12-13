package nomadrealms.context.game.actor;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.render.Renderable;
import nomadrealms.render.RenderingEnvironment;

import engine.common.math.Vector2f;

/**
 * An entity in the game world. Actors can have health, a position, an
 * inventory, and can be targeted by actions. They can also be rendered on the
 * screen.
 *
 * @author Lunkle
 */
public interface Actor extends HasPosition, HasHealth, HasInventory, Target, Renderable {

	default Vector2f getScreenPosition(RenderingEnvironment re) {
		return tile().getScreenPosition(re);
	}

	default List<Action> actions() {
		return new ArrayList<>();
	}

	default void update(GameState state) {
	}

	default List<InputEvent> retrieveNextPlays() {
		return new ArrayList<>();
	}

	default boolean isDestroyed() {
		return health() <= 0;
	}

}
