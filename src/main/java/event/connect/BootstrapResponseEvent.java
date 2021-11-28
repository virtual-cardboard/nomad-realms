package event.connect;

import common.source.NetworkSource;
import context.input.networking.packet.address.PacketAddress;
import event.network.NomadRealmsNetworkEvent;

public class BootstrapResponseEvent extends NomadRealmsNetworkEvent {

	private static final long serialVersionUID = 2102374617921382429L;

	private long nonce;
	private long timestamp;
	private PacketAddress lanAddress;
	private PacketAddress wanAddress;
	private String username;

	public BootstrapResponseEvent(NetworkSource source, long timestamp, long nonce, PacketAddress lanAddress, PacketAddress wanAddress, String username) {
		super(source);
		this.timestamp = timestamp;
		this.nonce = nonce;
		this.lanAddress = lanAddress;
		this.wanAddress = wanAddress;
		this.username = username;
	}

	@Override
	public NetworkSource source() {
		return (NetworkSource) super.source();
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

	public long timestamp() {
		return timestamp;
	}

	public String username() {
		return username;
	}

}
