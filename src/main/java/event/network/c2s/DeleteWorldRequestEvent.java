package event.network.c2s;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsC2SNetworkEvent;

public class DeleteWorldRequestEvent extends NomadRealmsC2SNetworkEvent {

	private long worldID;
	private String worldName;

	public DeleteWorldRequestEvent() {
	}

	public DeleteWorldRequestEvent(long worldID, String worldName) {
		this.worldID = worldID;
		this.worldName = worldName;
	}

	@Override
	public void read(SerializationReader reader) {
		this.worldID = reader.readLong();
		this.worldName = reader.readStringUtf8();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(worldID);
		writer.consume(worldName);
	}

	public long worldID() {
		return worldID;
	}

	public String worldName() {
		return worldName;
	}

}
