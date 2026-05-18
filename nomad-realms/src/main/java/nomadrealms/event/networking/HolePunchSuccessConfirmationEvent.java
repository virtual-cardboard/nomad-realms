package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.UUID;

/**
 * Sent between clients once a {@link HolePunchEvent} has been received, confirming a successful hole punch.
 * <p>
 * Sender: Client
 * <p>
 * Receiver: Client
 */
@Derializable
public class HolePunchSuccessConfirmationEvent implements SyncedEvent {

	private UUID nonce;

	public HolePunchSuccessConfirmationEvent() {
	}

	public HolePunchSuccessConfirmationEvent(UUID nonce) {
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
