//package event.network.bootstrap;
//
//import static networking.ClientNetworkUtils.LOCAL_HOST;
//import static networking.ClientNetworkUtils.toIP;
//import static networking.protocols.NomadRealmsP2PNetworkProtocol.BOOTSTRAP_REQUEST;
//
//import context.input.networking.packet.address.PacketAddress;
//import engine.common.loader.serialization.SerializationReader;
//import engine.common.source.NetworkSource;
//import event.network.NomadRealmsP2PNetworkEvent;
//import networking.protocols.NomadRealmsP2PNetworkProtocol;
//
//public class BootstrapRequestEvent extends NomadRealmsP2PNetworkEvent {
//
//	public final PacketAddress lanAddress;
//	public final String username;
//
//	public BootstrapRequestEvent(NetworkSource source, long timestamp, PacketAddress lanAddress, String username) {
//		super(timestamp, source);
//		this.lanAddress = lanAddress;
//		this.username = username;
//	}
//
//	public BootstrapRequestEvent(PacketAddress lanAddress, String username) {
//		super(LOCAL_HOST);
//		this.lanAddress = lanAddress;
//		this.username = username;
//	}
//
//	public BootstrapRequestEvent(NetworkSource source, PacketReader reader) {
//		super(source);
//		setTime(reader.readLong());
//		this.lanAddress = toIP(reader.readIPv4(), reader.readShort());
//		this.username = reader.readString();
//		reader.close();
//	}
//
//	@Override
//	protected NomadRealmsP2PNetworkProtocol protocol() {
//		return BOOTSTRAP_REQUEST;
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
