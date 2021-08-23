package networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.zip.DataFormatException;

import networking.packet.PacketModel;
import networking.packet.PacketReader;

public class UDPReceiver implements Runnable {

	private DatagramSocket socket;
	private Queue<PacketModel> packets = new LinkedList<>();
	private byte[] buffer = new byte[65536];
	private DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

	private boolean isDone = false;

	public UDPReceiver(DatagramSocket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		while (!isDone) {
			try {
				socket.receive(packet);
				packets.add(new PacketReader(packet).blocks());
			} catch (SocketTimeoutException e) {

			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataFormatException e) {
				e.printStackTrace();
			}
			Thread.yield();
		}
	}

}
