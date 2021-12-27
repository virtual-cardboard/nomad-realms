package event.network.peerconnect;

import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.NomadRealmsNetworkProtocol.PEER_CONNECT_REQUEST;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import event.network.NomadRealmsNetworkEvent;
import networking.protocols.NomadRealmsNetworkProtocol;

public class PeerConnectRequestEvent extends NomadRealmsNetworkEvent {

	private long nonce;
	private String username;

	public PeerConnectRequestEvent(long time, NetworkSource source, long nonce, String username) {
		super(time, source);
		this.nonce = nonce;
		this.username = username;
	}

	public PeerConnectRequestEvent(NetworkSource source, long nonce, String username) {
		super(source);
		this.nonce = nonce;
		this.username = username;
	}

	public PeerConnectRequestEvent(long nonce, String username) {
		this(LOCAL_HOST, nonce, username);
	}

	public PeerConnectRequestEvent(NetworkSource source, PacketReader reader) {
		super(source);
		setTime(reader.readLong());
		this.nonce = reader.readLong();
		this.username = reader.readString();
		reader.close();
	}

	@Override
	public NetworkSource source() {
		return super.source();
	}

	public long nonce() {
		return nonce;
	}

	@Override
	protected PacketModel toPacketModel(PacketBuilder builder) {
		return builder
				.consume(time())
				.consume(nonce)
				.consume(username)
				.build();
	}

	@Override
	protected NomadRealmsNetworkProtocol protocol() {
		return PEER_CONNECT_REQUEST;
	}

}
