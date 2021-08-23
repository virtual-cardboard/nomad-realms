package networking.packet.block;

public class PacketBlock {

	private byte[] buffer;

	public PacketBlock(byte[] buffer) {
		this.buffer = buffer;
	}

	public byte[] bytes() {
		return buffer;
	}

}
