package event.network.bootstrap;

import static context.input.networking.packet.PacketPrimitive.IP_V4;
import static context.input.networking.packet.PacketPrimitive.LONG;
import static context.input.networking.packet.PacketPrimitive.SHORT;
import static context.input.networking.packet.PacketPrimitive.STRING;
import static networking.NetworkUtils.LOCAL_HOST;
import static networking.NetworkUtils.toIP;
import static networking.protocols.ProtocolID.BOOTSTRAP_REQUEST;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import context.input.networking.packet.address.PacketAddress;
import event.network.NomadRealmsNetworkEvent;
import networking.protocols.ProtocolID;

public class BootstrapRequestEvent extends NomadRealmsNetworkEvent {

	/**
	 * protocol_id(100): timestamp, lan_ip, lan_port, username
	 */
	public static final PacketFormat BOOTSTRAP_REQUEST_FORMAT = new PacketFormat().with(LONG, IP_V4, SHORT, STRING);

	private PacketAddress lanAddress;
	private String username;

	public BootstrapRequestEvent(NetworkSource source, long timestamp, PacketAddress lanAddress, String username) {
		super(timestamp, source);
		this.lanAddress = lanAddress;
		this.username = username;
	}

	public BootstrapRequestEvent(PacketAddress lanAddress, String username) {
		super(LOCAL_HOST);
		this.lanAddress = lanAddress;
		this.username = username;
	}

	public BootstrapRequestEvent(NetworkSource source, PacketReader protocolReader) {
		super(source);
		PacketReader reader = BOOTSTRAP_REQUEST_FORMAT.reader(protocolReader);
		setTime(reader.readLong());
		this.lanAddress = toIP(reader.readIPv4(), reader.readShort());
		this.username = reader.readString();
		reader.close();
	}

	public PacketAddress lanAddress() {
		return lanAddress;
	}

	public String username() {
		return username;
	}

	@Override
	protected PacketModel toPacketModel(PacketBuilder builder) {
		return BOOTSTRAP_REQUEST_FORMAT.builder(builder)
				.consume(time())
				.consume(lanAddress.ip())
				.consume(lanAddress.shortPort())
				.consume(username)
				.build();
	}

	@Override
	protected ProtocolID protocolID() {
		return BOOTSTRAP_REQUEST;
	}

}
