package engine.networking;

import engine.context.input.networking.UDPSender;
import engine.context.input.networking.packet.PacketModel;
import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.event.networking.SyncedEventDerializer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;

public class NetworkingSender {

	private DatagramSocket socket;
	private UDPSender sender;
	private final ArrayBlockingQueue<PacketModel> networkSendBuffer = new ArrayBlockingQueue<>(100);
	private Thread senderThread;

	public void init() {
		try {
			socket = new DatagramSocket();
			sender = new UDPSender(socket, networkSendBuffer);
			senderThread = new Thread(sender);
			senderThread.setName("UDP Sender Thread");
			senderThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(SyncedEvent event, PacketAddress dest) {
		byte[] data = SyncedEventDerializer.serialize(event);
		networkSendBuffer.add(new PacketModel(data, dest));
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
