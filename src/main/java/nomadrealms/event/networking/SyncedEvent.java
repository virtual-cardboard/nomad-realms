package nomadrealms.event.networking;

import engine.context.input.networking.packet.PacketModel;
import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import nomadrealms.event.Event;

/**
 * These events must be sent to other players, but do not dictate the game simulation. They are used for other
 * non-time-sensitive events such as chat messages, camera movement, etc.
 */
@Derializable
public interface SyncedEvent extends Event {

	default PacketModel toPacket(PacketAddress address) {
		return new PacketModel(SyncedEventDerializer.serialize(this), address);
	}
}
