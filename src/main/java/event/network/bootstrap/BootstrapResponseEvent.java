package event.network.bootstrap;

import static networking.NetworkUtils.toIP;
import static networking.protocols.NomadRealmsNetworkProtocol.BOOTSTRAP_RESPONSE;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import context.input.networking.packet.address.PacketAddress;
import event.network.NomadRealmsNetworkEvent;
import networking.protocols.NomadRealmsNetworkProtocol;

public class BootstrapResponseEvent extends NomadRealmsNetworkEvent {

	private long nonce;
	private PacketAddress lanAddress;
	private PacketAddress wanAddress;
	private String username;

	public BootstrapResponseEvent(NetworkSource source, long timestamp, long nonce, PacketAddress lanAddress, PacketAddress wanAddress, String username) {
		super(timestamp, source);
		this.nonce = nonce;
		this.lanAddress = lanAddress;
		this.wanAddress = wanAddress;
		this.username = username;
	}

	public BootstrapResponseEvent(NetworkSource source, PacketReader reader) {
		super(source);
		setTime(reader.readLong());
		this.nonce = reader.readLong();
		this.lanAddress = toIP(reader.readIPv4(), reader.readShort());
		this.wanAddress = toIP(reader.readIPv4(), reader.readShort());
		this.username = reader.readString();
		reader.close();
	}

	public PacketAddress lanAddress() {
		return lanAddress;
	}

	public PacketAddress wanAddress() {
		return wanAddress;
	}

	public long nonce() {
		return nonce;
	}

	public String username() {
		return username;
	}

	@Override
	protected PacketModel toPacketModel(PacketBuilder builder) {
		return builder
				.consume(time())
				.consume(nonce)
				.consume(lanAddress.ip())
				.consume(lanAddress.shortPort())
				.consume(wanAddress.ip())
				.consume(wanAddress.shortPort())
				.consume(username)
				.build();
	}

	@Override
	protected NomadRealmsNetworkProtocol protocol() {
		return BOOTSTRAP_RESPONSE;
	}

	@Override
	public String toString() {
		return "BootstrapResponseEvent [nonce=" + nonce + ", lanAddress=" + lanAddress + ", wanAddress=" + wanAddress + ", username=" + username + "]";
	}

}
