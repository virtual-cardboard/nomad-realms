package nomadrealms.event.networking.bootstrap;

import engine.serialization.Derializable;
import java.util.List;
import java.util.ArrayList;
import nomadrealms.user.Player;

@Derializable
public class OnlinePlayersListEvent extends BootstrapEvent {

	private List<Player> players;

	protected OnlinePlayersListEvent() {
		this.players = new ArrayList<>();
	}

	public OnlinePlayersListEvent(List<Player> players) {
		this.players = players;
	}

	public List<Player> players() {
		return players;
	}

}
