package engine.networking.messenger;

import engine.context.input.networking.UDPSender;
import engine.context.input.networking.packet.PacketModel;
import engine.context.input.networking.packet.address.PacketAddress;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import nomadrealms.event.networking.SyncedEvent;

public class NetworkSender {

	private DatagramSocket socket;
	private UDPSender sender;
	private final ArrayBlockingQueue<PacketModel> networkSendBuffer = new ArrayBlockingQueue<>(100);
	private Thread senderThread;

	public void init() {
		init(null);
	}

	public void init(DatagramSocket socket) {
		try {
			this.socket = (socket == null) ? new DatagramSocket() : socket;
			sender = new UDPSender(this.socket, networkSendBuffer);
			senderThread = new Thread(sender);
			senderThread.setName("UDP Sender Thread");
			senderThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(SyncedEvent event, PacketAddress dest) {
		networkSendBuffer.add(event.toPacket(dest));
	}

	public void cleanUp() {
		if (sender != null) {
			sender.terminate();
		}
		if (senderThread != null) {
			try {
				senderThread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

}
