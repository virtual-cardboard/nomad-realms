//package event.network.world;
//
//import static networking.ClientNetworkUtils.LOCAL_HOST;
//import static networking.protocols.NomadRealmsP2PNetworkProtocol.CREATE_WORLD_RESPONSE;
//
//import context.input.networking.packet.PacketModel;
//import engine.common.loader.serialization.SerializationReader;
//import engine.common.source.NetworkSource;
//import event.network.NomadRealmsP2PNetworkEvent;
//import networking.protocols.NomadRealmsP2PNetworkProtocol;
//
//public class CreateWorldResponseEvent extends NomadRealmsP2PNetworkEvent {
//
//	public final long seed;
//
//	public CreateWorldResponseEvent(NetworkSource source, long timestamp, long seed) {
//		super(timestamp, source);
//		this.seed = seed;
//	}
//
//	public CreateWorldResponseEvent(long seed) {
//		super(LOCAL_HOST);
//		this.seed = seed;
//	}
//
//	public CreateWorldResponseEvent(NetworkSource source, PacketReader reader) {
//		super(source);
//		setTime(reader.readLong());
//		this.seed = reader.readLong();
//		reader.close();
//	}
//
//	protected PacketModel toPacketModel(PacketBuilder builder) {
//		return builder
//				.consume(time())
//				.consume(seed)
//				.build();
//	}
//
//	@Override
//	protected NomadRealmsP2PNetworkProtocol protocol() {
//		return CREATE_WORLD_RESPONSE;
//	}
//
//	@Override
//	public void read(SerializationReader reader) {
//
//	}
//
//	@Override
//	public byte[] serialize() {
//		return new byte[0];
//	}
//}
