package event.network.p2p.world;

import static networking.protocols.NomadRealmsC2SNetworkProtocol.CREATE_WORLD_REQUEST_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsC2SNetworkEvent;
import networking.protocols.NomadRealmsC2SNetworkProtocol;

public class CreateWorldRequestEvent extends NomadRealmsC2SNetworkEvent {

	private long seed;
	private String worldName;

	public CreateWorldRequestEvent() {
	}

	public CreateWorldRequestEvent(long seed, String worldName) {
		this.seed = seed;
		this.worldName = worldName;
	}

	public CreateWorldRequestEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsC2SNetworkProtocol formatEnum() {
		return CREATE_WORLD_REQUEST_EVENT;
	}

	@Override
	public void read(SerializationReader reader) {
		this.seed = reader.readLong();
		this.worldName = reader.readStringUtf8();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(seed);
		writer.consume(worldName);
	}

	public long seed() {
		return seed;
	}

	public String worldName() {
		return worldName;
	}

}