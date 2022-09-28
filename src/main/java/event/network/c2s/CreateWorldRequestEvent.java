package event.network.c2s;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsC2SNetworkEvent;

public class CreateWorldRequestEvent extends NomadRealmsC2SNetworkEvent {

	private long seed;
	private String worldName;

	public CreateWorldRequestEvent() {
	}

	public CreateWorldRequestEvent(long seed, String worldName) {
		this.seed = seed;
		this.worldName = worldName;
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
