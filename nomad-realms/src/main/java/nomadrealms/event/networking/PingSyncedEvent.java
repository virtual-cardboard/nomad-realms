package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;

@Derializable
public class PingSyncedEvent implements SyncedEvent {

	private String message;
	private long timestamp;

	public PingSyncedEvent() {
	}

	public PingSyncedEvent(String message, long timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}

	public String message() {
		return message;
	}

	public long timestamp() {
		return timestamp;
	}


	public String toString() {
		return "PingSyncedEvent{" +
				"message='" + message + '\'' +
				", timestamp=" + timestamp +
				'}';
	}

}
