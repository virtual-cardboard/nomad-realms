package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.List;
import nomadrealms.user.Player;

@Derializable
public class PlayerListSyncedEvent implements SyncedEvent {

	private List<Player> players;

	public PlayerListSyncedEvent() {
	}

	public PlayerListSyncedEvent(List<Player> players) {
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
