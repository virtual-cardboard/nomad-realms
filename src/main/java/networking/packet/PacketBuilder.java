package networking.packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import networking.packet.block.PacketBlock;
import networking.packet.destination.PacketDestination;

public class PacketBuilder {

	private PacketDestination dest;
	private PacketBlock[] blocks;

	public PacketBuilder(PacketDestination dest, PacketBlock... blocks) {
		this.dest = dest;
		this.blocks = blocks;
	}

	/**
	 * Constructs the actual {@link DatagramPacket} that can be sent from a
	 * {@link DatagramSocket}. Should only be called once for efficiency.
	 * 
	 * @return
	 */
	public DatagramPacket packet() {
		int totalSize = 0;
		for (int i = 0, numBlocks = blocks.length; i < numBlocks; i++) {
			totalSize += 2 + blocks[i].bytes().length; // 2 bytes for length of block + actual bytes
		}
		byte[] buffer = new byte[totalSize];
		int index = 0;
		for (int i = 0, numBlocks = blocks.length; i < numBlocks; i++) {
			PacketBlock block = blocks[i];
			byte[] bytes = block.bytes();
			buffer[index] = (byte) (bytes.length >> 8);
			buffer[index + 1] = (byte) bytes.length;
			System.arraycopy(bytes, 0, buffer, index + 2, bytes.length);
			index += 2 + bytes.length;
		}
		DatagramPacket datagramPacket = new DatagramPacket(buffer, totalSize, dest.ip(), dest.port());
		return datagramPacket;
	}

}
