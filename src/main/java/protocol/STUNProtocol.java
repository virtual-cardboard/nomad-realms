package protocol;

import static context.input.networking.packet.block.PacketPrimitive.IP_V4;
import static context.input.networking.packet.block.PacketPrimitive.LONG;
import static context.input.networking.packet.block.PacketPrimitive.SHORT;

import context.input.networking.packet.block.PacketBlockFormat;

public class STUNProtocol {

	public static final PacketBlockFormat STUN_REQUEST = new PacketBlockFormat().with(LONG, LONG); // timestamp, nonce
	public static final PacketBlockFormat STUN_RESPONSE = new PacketBlockFormat().with(LONG, LONG, IP_V4, SHORT); // timestamp, nonce, ip, port

}
