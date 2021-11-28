package event.network.peerconnect;

import static context.input.networking.packet.PacketPrimitive.LONG;
import static context.input.networking.packet.PacketPrimitive.STRING;
import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.ProtocolID.PEER_CONNECT_REQUEST;

import java.io.Serializable;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import event.network.NomadRealmsNetworkEvent;
import networking.protocols.ProtocolID;

public class PeerConnectRequestEvent extends NomadRealmsNetworkEvent implements Serializable {

	private static final long serialVersionUID = 3967611254630803156L;

	/**
	 * protocol_id(150): timestamp, nonce, username
	 */
	public static final PacketFormat PEER_CONNECT_REQUEST_FORMAT = new PacketFormat().with(LONG, LONG, STRING);

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

	public PeerConnectRequestEvent(NetworkSource source, PacketReader protocolReader) {
		super(source);
		PacketReader reader = PEER_CONNECT_REQUEST_FORMAT.reader(protocolReader);
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
		return PEER_CONNECT_REQUEST_FORMAT.builder(builder)
				.consume(time())
				.consume(nonce)
				.consume(username)
				.build();
	}

	@Override
	protected ProtocolID protocolID() {
		return PEER_CONNECT_REQUEST;
	}

}
