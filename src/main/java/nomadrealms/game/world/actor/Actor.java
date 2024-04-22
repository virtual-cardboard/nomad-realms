package nomadrealms.game.world.actor;

import nomadrealms.game.event.Target;
import nomadrealms.render.Renderable;

public interface Actor extends HasPosition, HasHealth, Target, Renderable {

    void update();
}
