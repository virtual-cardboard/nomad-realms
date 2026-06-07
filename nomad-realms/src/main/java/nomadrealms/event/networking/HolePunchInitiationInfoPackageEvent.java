package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import java.util.Map;
import java.util.UUID;

/**
 * Sent by the server to the initiating client, containing the nonce and public IP address for each peer.
 * <p>
 * Sender: Server
 * <p>
 * Receiver: Client (Initiator)
 */
@Derializable
public class HolePunchInitiationInfoPackageEvent implements SyncedEvent {

	private Map<UUID, PacketAddress> nonceToAddress;

	public HolePunchInitiationInfoPackageEvent() {
	}

	public HolePunchInitiationInfoPackageEvent(Map<UUID, PacketAddress> nonceToAddress) {
		this.nonceToAddress = nonceToAddress;
	}

	public Map<UUID, PacketAddress> nonceToAddress() {
		return nonceToAddress;
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
