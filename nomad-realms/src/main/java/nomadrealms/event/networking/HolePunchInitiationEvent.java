package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;

/**
 * Sent by a client to the server to initiate the UDP hole punching process with all other online players.
 * <p>
 * Sender: Client
 * <p>
 * Receiver: Server
 */
@Derializable
public class HolePunchInitiationEvent implements SyncedEvent {

	public HolePunchInitiationEvent() {
	}


}
