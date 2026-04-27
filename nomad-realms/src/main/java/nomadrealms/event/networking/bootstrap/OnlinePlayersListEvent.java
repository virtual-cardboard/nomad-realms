package nomadrealms.event.networking.bootstrap;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.ArrayList;
import java.util.List;
import nomadrealms.event.networking.SyncedEventHandler;
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

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
