package nomadrealms.event.networking.bootstrap;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.Arrays;
import java.util.List;
import nomadrealms.user.Player;

@Derializable
public class OnlinePlayersListEvent extends BootstrapEvent {

	private Player[] players;

	protected OnlinePlayersListEvent() {
		this.players = new Player[0];
	}

	public OnlinePlayersListEvent(List<Player> players) {
		this.players = players.toArray(new Player[0]);
	}

	public List<Player> players() {
		return Arrays.asList(players);
	}


}
