package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;

@Derializable
public class PongSyncedEvent implements SyncedEvent {

	private String message;
	private long timestamp;

	public PongSyncedEvent() {
	}

	public PongSyncedEvent(String message, long timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}

	public String message() {
		return message;
	}

	public long timestamp() {
		return timestamp;
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

	@Override
	public String toString() {
		return "PongSyncedEvent{" +
				"message='" + message + '\'' +
				", timestamp=" + timestamp +
				'}';
	}

}
