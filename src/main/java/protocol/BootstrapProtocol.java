package protocol;

import static context.input.networking.packet.PacketPrimitive.IP_V4;
import static context.input.networking.packet.PacketPrimitive.LONG;
import static context.input.networking.packet.PacketPrimitive.SHORT;

import context.input.networking.packet.PacketFormat;

public class BootstrapProtocol {

	/**
	 * timestamp, lan_ip, lan_port
	 */
	public static final PacketFormat BOOTSTRAP_REQUEST = new PacketFormat().with(LONG, IP_V4, SHORT);

	/**
	 * timestamp, nonce, lan_ip, lan_port, wan_ip, wan_port
	 */
	public static final PacketFormat BOOTSTRAP_RESPONSE = new PacketFormat().with(LONG, LONG, IP_V4, SHORT, IP_V4, SHORT);

	/**
	 * timestamp, nonce
	 */
	public static final PacketFormat PEER_CONNECT_REQUEST = new PacketFormat().with(LONG, LONG);

	/**
	 * timestamp, nonce
	 */
	public static final PacketFormat PEER_CONNECT_ACK = new PacketFormat().with(LONG, LONG);

}
