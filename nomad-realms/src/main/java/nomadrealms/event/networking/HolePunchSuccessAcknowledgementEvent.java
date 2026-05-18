package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.UUID;

/**
 * Sent as an acknowledgement of receiving a {@link HolePunchSuccessConfirmationEvent}.
 * <p>
 * Sender: Client
 * <p>
 * Receiver: Client
 */
@Derializable
public class HolePunchSuccessAcknowledgementEvent implements SyncedEvent {

	private UUID nonce;

	public HolePunchSuccessAcknowledgementEvent() {
	}

	public HolePunchSuccessAcknowledgementEvent(UUID nonce) {
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
