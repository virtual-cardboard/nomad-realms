package engine.networking;

import engine.context.input.networking.SocketFinder;
import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.event.networking.SyncedEvent;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NetworkNode {

	private final NetworkingSender sender = new NetworkingSender();
	private final NetworkingReceiver receiver = new NetworkingReceiver();

	public void init() {
		try {
			DatagramSocket socket = SocketFinder.findSocket(0);
			sender.init(socket);
			receiver.init(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void init(int port) {
		try {
			DatagramSocket socket = SocketFinder.findSocket(port);
			sender.init(socket);
			receiver.init(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(SyncedEvent event, PacketAddress dest) {
		sender.send(event, dest);
	}

	public void update(Consumer<SyncedEvent> consumer) {
		receiver.update(consumer);
	}

	public void update(BiConsumer<SyncedEvent, PacketAddress> consumer) {
		receiver.update(consumer);
	}

	public void cleanUp() {
		sender.cleanUp();
		receiver.cleanUp();
	}
}
