package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.UUID;

/**
 * Sent between clients to perform the actual UDP hole punching.
 * <p>
 * Sender: Client
 * <p>
 * Receiver: Client
 */
@Derializable
public class HolePunchEvent implements SyncedEvent {

	private UUID nonce;

	public HolePunchEvent() {
	}

	public HolePunchEvent(UUID nonce) {
		this.nonce = nonce;
	}

	public UUID nonce() {
		return nonce;
	}


}
