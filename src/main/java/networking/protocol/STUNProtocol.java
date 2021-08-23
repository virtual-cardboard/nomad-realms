package networking.protocol;

import static networking.packet.block.PacketPrimitive.IP_V4;
import static networking.packet.block.PacketPrimitive.SHORT;

import networking.packet.block.PacketBlockFormat;

public class STUNProtocol {

	public static final PacketBlockFormat STUN_REQUEST = new PacketBlockFormat().with(IP_V4, SHORT);

}
