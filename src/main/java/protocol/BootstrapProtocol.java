package protocol;

import static context.input.networking.packet.block.PacketPrimitive.IP_V4;
import static context.input.networking.packet.block.PacketPrimitive.LONG;
import static context.input.networking.packet.block.PacketPrimitive.SHORT;

import context.input.networking.packet.block.PacketBlockFormat;

public class BootstrapProtocol {

	/**
	 * timestamp, lan_ip, lan_port
	 */
	public static final PacketBlockFormat BOOTSTRAP_REQUEST = new PacketBlockFormat().with(LONG, IP_V4, SHORT);

	/**
	 * timestamp, nonce, lan_ip, lan_port, wan_ip, wan_port
	 */
	public static final PacketBlockFormat BOOTSTRAP_RESPONSE = new PacketBlockFormat().with(LONG, LONG, IP_V4, SHORT, IP_V4, SHORT);

	/**
	 * timestamp, nonce, ip, port
	 */
	public static final PacketBlockFormat PEER_CONNECT_REQUEST = new PacketBlockFormat().with(LONG, LONG, IP_V4, SHORT, IP_V4, SHORT);

	/**
	 * timestamp, nonce
	 */
	public static final PacketBlockFormat PEER_CONNECT_ACK = new PacketBlockFormat().with(LONG, LONG, IP_V4, SHORT);

}
