package networking.protocol;

import static networking.packet.block.PacketPrimitive.SHORT;

import networking.packet.block.PacketBlockFormat;

public class STUNProtocol {

	public static final PacketBlockFormat STUN_REQUEST = new PacketBlockFormat().with(SHORT);

}
