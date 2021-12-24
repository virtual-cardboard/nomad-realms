package event.network.world;

import static context.input.networking.packet.PacketPrimitive.LONG;
import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.ProtocolID.CREATE_WORLD_RESPONSE;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import event.network.NomadRealmsNetworkEvent;
import networking.protocols.ProtocolID;

public class CreateWorldResponseEvent extends NomadRealmsNetworkEvent {

	/**
	 * protocol_id(111): timestamp, seed
	 */
	public static final PacketFormat CREATE_WORLD_RESPONSE_FORMAT = new PacketFormat().with(LONG, LONG);

	private long seed;

	public CreateWorldResponseEvent(NetworkSource source, long timestamp, long seed) {
		super(timestamp, source);
		this.seed = seed;
	}

	public CreateWorldResponseEvent(long seed) {
		super(LOCAL_HOST);
		this.seed = seed;
	}

	public CreateWorldResponseEvent(NetworkSource source, PacketReader protocolReader) {
		super(source);
		PacketReader reader = CREATE_WORLD_RESPONSE_FORMAT.reader(protocolReader);
		setTime(reader.readLong());
		this.seed = reader.readLong();
		reader.close();
	}

	public long seed() {
		return seed;
	}

	@Override
	protected PacketModel toPacketModel(PacketBuilder builder) {
		return CREATE_WORLD_RESPONSE_FORMAT.builder(builder)
				.consume(time())
				.consume(seed)
				.build();
	}

	@Override
	protected ProtocolID protocolID() {
		return CREATE_WORLD_RESPONSE;
	}

}
