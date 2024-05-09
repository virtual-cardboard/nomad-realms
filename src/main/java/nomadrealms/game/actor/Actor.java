package nomadrealms.game.actor;

import nomadrealms.game.GameState;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.Target;
import nomadrealms.render.Renderable;

import java.util.ArrayList;
import java.util.List;

public interface Actor extends HasPosition, HasHealth, HasInventory, Target, Renderable {

    default void update(GameState state) {
    }

    default List<InputEvent> retrieveNextPlays() {
        return new ArrayList<>();
    }

}
