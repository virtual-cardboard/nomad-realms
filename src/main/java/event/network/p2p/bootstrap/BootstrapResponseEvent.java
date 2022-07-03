//package event.network.bootstrap;
//
//import static networking.protocols.NomadRealmsP2PNetworkProtocol.BOOTSTRAP_RESPONSE_EVENT;
//
//import context.input.networking.packet.address.PacketAddress;
//import engine.common.loader.serialization.SerializationReader;
//import engine.common.loader.serialization.SerializationWriter;
//import event.network.NomadRealmsP2PNetworkEvent;
//import networking.protocols.NomadRealmsP2PNetworkProtocol;
//
//public class BootstrapResponseEvent extends NomadRealmsP2PNetworkEvent {
//
//	private long timestamp;
//	private long nonce;
//	private String username;
//	private PacketAddress lanAddress;
//	private PacketAddress wanAddress;
//
//	public BootstrapResponseEvent() {
//	}
//
//	public BootstrapResponseEvent(long timestamp, long nonce, String username, PacketAddress lanAddress, PacketAddress wanAddress) {
//		this.timestamp = timestamp;
//		this.nonce = nonce;
//		this.username = username;
//		this.lanAddress = lanAddress;
//		this.wanAddress = wanAddress;
//	}
//
//	@Override
//	public void read(SerializationReader reader) {
//		this.timestamp = reader.readLong();
//		this.nonce = reader.readLong();
//		this.username = reader.readStringUtf8();
//		this.lanAddress = new PacketAddress();
//		this.lanAddress.read(reader);
//		this.wanAddress = new PacketAddress();
//		this.wanAddress.read(reader);
//	}
//
//	@Override
//	public void write(SerializationWriter writer) {
//		writer.consume(timestamp);
//		writer.consume(nonce);
//		writer.consume(username);
//		lanAddress.write(writer);
//		wanAddress.write(writer);
//	}
//
//	public long timestamp() {
//		return timestamp;
//	}
//
//	public long nonce() {
//		return nonce;
//	}
//
//	public String username() {
//		return username;
//	}
//
//	public PacketAddress lanAddress() {
//		return lanAddress;
//	}
//
//	public PacketAddress wanAddress() {
//		return wanAddress;
//	}
//
//	@Override
//	protected NomadRealmsP2PNetworkProtocol protocol() {
//		return BOOTSTRAP_RESPONSE_EVENT;
//	}
//
//	@Override
//	public String toString() {
//		return "BootstrapResponseEvent [nonce=" + nonce + ", lanAddress=" + lanAddress + ", wanAddress=" + wanAddress + ", username=" + username + "]";
//	}
//
//}
