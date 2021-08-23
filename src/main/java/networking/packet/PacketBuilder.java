package networking.packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.List;

import networking.packet.block.PacketBlock;
import networking.packet.destination.PacketDestination;

public class PacketBuilder {

	private PacketModel model;
	private byte[] buffer;

	public PacketBuilder(PacketModel model) {
		this.model = model;
	}

	public PacketBuilder(PacketModel model, byte[] buffer) {
		this(model);
		this.buffer = buffer;
	}

	/**
	 * Constructs the actual {@link DatagramPacket} that can be sent from a
	 * {@link DatagramSocket}. Should only be called once for efficiency.
	 * 
	 * @return
	 */
	public DatagramPacket packet() {
		PacketDestination dest = model.dest();
		List<PacketBlock> blocks = model.blocks();
		if (buffer == null) {
			int totalSize = 0;
			for (int i = 0, numBlocks = blocks.size(); i < numBlocks; i++) {
				totalSize += 2 + blocks.get(i).bytes().length; // 2 bytes for length of block + actual bytes
			}
			buffer = new byte[totalSize];
		}
		int index = 0;
		for (int i = 0, numBlocks = blocks.size(); i < numBlocks; i++) {
			PacketBlock block = blocks.get(i);
			byte[] bytes = block.bytes();
			buffer[index] = (byte) (bytes.length >> 8);
			buffer[index + 1] = (byte) bytes.length;
			System.arraycopy(bytes, 0, buffer, index + 2, bytes.length);
			index += 2 + bytes.length;
		}
		DatagramPacket datagramPacket = new DatagramPacket(buffer, index, dest.ip(), dest.port());
		return datagramPacket;
	}

}
