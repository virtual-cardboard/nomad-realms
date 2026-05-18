package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.UUID;
import nomadrealms.user.Player;

/**
 * Sent by the server to all peers of an initiating client to inform them about the intent to hole punch.
 * <p>
 * Sender: Server
 * <p>
 * Receiver: Client (Peer)
 */
@Derializable
public class HolePunchInitiationIntentEvent implements SyncedEvent {

	private UUID nonce;
	private Player initiator;

	public HolePunchInitiationIntentEvent() {
	}

	public HolePunchInitiationIntentEvent(UUID nonce, Player initiator) {
		this.nonce = nonce;
		this.initiator = initiator;
	}

	public UUID nonce() {
		return nonce;
	}

	public Player initiator() {
		return initiator;
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
