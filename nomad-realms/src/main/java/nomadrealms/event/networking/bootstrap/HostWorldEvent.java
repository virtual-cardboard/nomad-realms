package nomadrealms.event.networking.bootstrap;

import java.util.List;
import nomadrealms.context.game.world.World;
import nomadrealms.user.Player;

/**
 * This is sent to the bootstrapping server to indicate that this {@link World} is available for joining
 */
public class HostWorldEvent {

	List<Player> players;

}
