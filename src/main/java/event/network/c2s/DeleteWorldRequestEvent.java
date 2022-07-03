package event.network.c2s;

import static networking.protocols.NomadRealmsC2SNetworkProtocol.DELETE_WORLD_REQUEST_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsC2SNetworkEvent;
import networking.protocols.NomadRealmsC2SNetworkProtocol;

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

	@Override
	public NomadRealmsC2SNetworkProtocol formatEnum() {
		return DELETE_WORLD_REQUEST_EVENT;
	}

}
