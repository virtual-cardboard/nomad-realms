package engine.networking;

import static engine.context.input.networking.SocketFinder.findSocket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.function.Consumer;

import engine.context.input.event.PacketReceivedInputEvent;
import engine.context.input.networking.UDPReceiver;

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

	public void update(Consumer<PacketReceivedInputEvent> consumer) {
		while (!networkReceiveBuffer.isEmpty()) {
			consumer.accept(networkReceiveBuffer.poll());
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
