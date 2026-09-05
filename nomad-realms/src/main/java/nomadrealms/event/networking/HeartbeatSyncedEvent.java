package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.UUID;

/**
 * Periodically sent between connected nodes to indicate that the connection is active.
 */
@Derializable
public class HeartbeatSyncedEvent implements SyncedEvent {

	private UUID nonce;

	public HeartbeatSyncedEvent() {
	}

	public HeartbeatSyncedEvent(UUID nonce) {
		this.nonce = nonce;
	}

	public UUID nonce() {
		return nonce;
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
