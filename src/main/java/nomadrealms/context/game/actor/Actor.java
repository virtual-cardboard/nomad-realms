package nomadrealms.context.game.actor;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.context.game.GameState;
import nomadrealms.context.game.card.action.Action;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.Target;
import nomadrealms.context.game.world.map.area.Tile;
import nomadrealms.render.Renderable;

/**
 * An entity in the game world. Actors can have health, a position, an inventory, and can be targeted by actions. They
 * can also be rendered on the screen.
 *
 * @author Lunkle
 */
public interface Actor extends HasPosition, HasHealth, HasInventory, Target, Renderable {

	String name();

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

	@Override
	default boolean move(Tile target) {
		if (target.actor() != null) {
			return false;
		}
		if (tile() != null) {
			tile().clearActor();
		}
		if (!HasPosition.super.move(target)) {
			return false;
		}
		target.actor(this);
		return true;
	}

}
