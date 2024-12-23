package nomadrealms.game.actor;

import java.util.ArrayList;
import java.util.List;

import nomadrealms.game.GameState;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.Target;
import nomadrealms.render.Renderable;

public interface Actor extends HasPosition, HasHealth, HasInventory, Target, Renderable {

	default void update(GameState state) {
	}

	default List<InputEvent> retrieveNextPlays() {
		return new ArrayList<>();
	}

	default boolean isDestroyed() {
		return health() <= 0;
	}

}
