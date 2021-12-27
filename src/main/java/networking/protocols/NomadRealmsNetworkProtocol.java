package networking.protocols;

import static context.input.networking.packet.PacketPrimitive.IP_V4;
import static context.input.networking.packet.PacketPrimitive.LONG;
import static context.input.networking.packet.PacketPrimitive.SHORT;
import static context.input.networking.packet.PacketPrimitive.STRING;

import context.input.networking.packet.PacketFormat;
import event.network.NomadRealmsNetworkEvent;
import event.network.bootstrap.BootstrapRequestEvent;
import event.network.bootstrap.BootstrapResponseEvent;
import event.network.game.CardHoveredNetworkEvent;
import event.network.game.CardPlayedNetworkEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import event.network.peerconnect.PeerConnectResponseEvent;
import event.network.world.CreateWorldRequestEvent;
import event.network.world.CreateWorldResponseEvent;

public enum NomadRealmsNetworkProtocol {

	/** protocol_id(100): timestamp, lan_ip, lan_port, username */
	BOOTSTRAP_REQUEST(BootstrapRequestEvent.class, 100, new PacketFormat().with(LONG, IP_V4, SHORT, STRING)),

	/** id(101): timestamp, nonce, lan_ip, lan_port, wan_ip, wan_port, username */
	BOOTSTRAP_RESPONSE(BootstrapResponseEvent.class, 101, new PacketFormat().with(LONG, LONG, IP_V4, SHORT, IP_V4, SHORT, STRING)),

	/** protocol_id(110): timestamp, world_name */
	CREATE_WORLD_REQUEST(CreateWorldRequestEvent.class, 110, new PacketFormat().with(LONG, STRING)),

	/** protocol_id(111): timestamp, seed */
	CREATE_WORLD_RESPONSE(CreateWorldResponseEvent.class, 111, new PacketFormat().with(LONG, LONG)),

	/** protocol_id(150): timestamp, nonce, username */
	PEER_CONNECT_REQUEST(PeerConnectRequestEvent.class, 120, new PacketFormat().with(LONG, LONG, STRING)),

	/** protocol_id(151): timestamp, nonce, username */
	PEER_CONNECT_RESPONSE(PeerConnectResponseEvent.class, 121, new PacketFormat().with(LONG, LONG, STRING)),

	/** protocol_id(150): timestamp, player_id, target_id, card_id */
	CARD_PLAYED(CardPlayedNetworkEvent.class, 150, new PacketFormat().with(LONG, LONG, LONG, LONG)),

	/** protocol_id(150): timestamp, player_id, card_id */
	CARD_HOVERED(CardHoveredNetworkEvent.class, 151, new PacketFormat().with(LONG, LONG, LONG));

	private short id;
	private Class<? extends NomadRealmsNetworkEvent> clazz;
	private PacketFormat format;

	private NomadRealmsNetworkProtocol(Class<? extends NomadRealmsNetworkEvent> clazz, int id, PacketFormat format) {
		this.clazz = clazz;
		this.id = (short) id;
		this.format = format;
	}

	public short id() {
		return id;
	}

	public Class<? extends NomadRealmsNetworkEvent> clazz() {
		return clazz;
	}

	public PacketFormat format() {
		return format;
	}

}
