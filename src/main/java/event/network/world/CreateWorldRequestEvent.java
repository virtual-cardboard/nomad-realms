package event.network.world;

import static networking.protocols.NomadRealmsNetworkProtocol.CREATE_WORLD_REQUEST;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import event.network.NomadRealmsNetworkEvent;
import networking.protocols.NomadRealmsNetworkProtocol;

public class CreateWorldRequestEvent extends NomadRealmsNetworkEvent {

	private String worldName;

	public CreateWorldRequestEvent(NetworkSource source, long timestamp, String worldName) {
		super(timestamp, source);
		this.worldName = worldName;
	}

	public CreateWorldRequestEvent(NetworkSource source, PacketReader reader) {
		super(source);
		this.worldName = reader.readString();
		reader.close();
	}

	public String worldName() {
		return worldName;
	}

	@Override
	protected PacketModel toPacketModel(PacketBuilder builder) {
		return builder
				.consume(time())
				.consume(worldName)
				.build();
	}

	@Override
	protected NomadRealmsNetworkProtocol protocol() {
		return CREATE_WORLD_REQUEST;
	}

}
