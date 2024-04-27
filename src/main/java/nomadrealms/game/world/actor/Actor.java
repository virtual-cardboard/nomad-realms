package nomadrealms.game.world.actor;

import nomadrealms.game.GameState;
import nomadrealms.game.event.InputEvent;
import nomadrealms.game.event.Target;
import nomadrealms.render.Renderable;

import java.util.ArrayList;
import java.util.List;

public interface Actor extends HasPosition, HasHealth, Target, Renderable {

    default List<InputEvent> update(GameState state) {
        return new ArrayList<>();
    }

    default List<InputEvent> retrieveNextPlays() {
        return new ArrayList<>();
    }

}
