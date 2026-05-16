package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;

@Derializable
public class JoinGameSyncedEvent implements SyncedEvent {

	private String name;

	public JoinGameSyncedEvent() {
	}

	public JoinGameSyncedEvent(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
