package engine.networking;

import static engine.context.input.networking.SocketFinder.findSocket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.BiConsumer;

import engine.context.input.event.PacketReceivedInputEvent;
import engine.context.input.networking.NetworkSource;
import engine.context.input.networking.UDPReceiver;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.event.networking.SyncedEventDerializer;

public class NetworkingReceiver {

	private DatagramSocket socket;
	private UDPReceiver receiver;
	private final ArrayBlockingQueue<PacketReceivedInputEvent> networkReceiveBuffer = new ArrayBlockingQueue<>(100);
	private Thread receiverThread;

	public void init(int port) {
		try {
			socket = findSocket(port);
			receiver = new UDPReceiver(socket, networkReceiveBuffer);
			receiverThread = new Thread(receiver);
			receiverThread.setName("UDP Receiver Thread");
			receiverThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void update(BiConsumer<SyncedEvent, NetworkSource> consumer) {
		while (!networkReceiveBuffer.isEmpty()) {
			PacketReceivedInputEvent packetEvent = networkReceiveBuffer.poll();
			SyncedEvent event = SyncedEventDerializer.deserialize(packetEvent.model().bytes());
			consumer.accept(event, packetEvent.source());
		}
	}

	public void cleanUp() {
		if (receiver != null) {
			receiver.terminate();
		}
		if (socket != null) {
			socket.close();
		}
		if (receiverThread != null) {
			try {
				receiverThread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		}
	}

}
