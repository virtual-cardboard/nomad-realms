package networking;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import networking.packet.PacketBuilder;
import networking.packet.PacketModel;

public class UDPSender implements Runnable {

	private DatagramSocket socket;
	private Queue<PacketModel> packets = new ConcurrentLinkedQueue<>();
	private byte[] buffer = new byte[65536];
	private boolean isDone = false;

	public UDPSender(DatagramSocket socket) {
		this.socket = socket;
	}

	public void sendAsync(PacketModel packet) {
		packets.add(packet);
	}

	@Override
	public void run() {
		while (!isDone) {
			while (!packets.isEmpty()) {
				try {
					socket.send(new PacketBuilder(packets.poll(), buffer).packet());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			Thread.yield();
		}
	}

}
