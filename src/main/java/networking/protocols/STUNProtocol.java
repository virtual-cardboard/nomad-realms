package networking.protocols;

import static context.input.networking.packet.PacketPrimitive.IP_V4;
import static context.input.networking.packet.PacketPrimitive.LONG;
import static context.input.networking.packet.PacketPrimitive.SHORT;

import context.input.networking.packet.PacketFormat;

public class STUNProtocol {

	public static final PacketFormat STUN_REQUEST = new PacketFormat().with(LONG, LONG); // timestamp, nonce
	public static final PacketFormat STUN_RESPONSE = new PacketFormat().with(LONG, LONG, IP_V4, SHORT); // timestamp, nonce, ip, port

}
