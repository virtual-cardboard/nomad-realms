package engine.networking.messenger;

import engine.context.input.networking.SocketFinder;
import engine.context.input.networking.packet.address.PacketAddress;
import java.io.IOException;
import java.net.DatagramSocket;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import nomadrealms.event.networking.SyncedEvent;

public class NetworkMessenger {

	private final NetworkSender sender = new NetworkSender();
	private final NetworkReceiver receiver = new NetworkReceiver();

	public void init() {
		init(0);
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

	public void send(SyncedEvent event, PacketAddress address) {
		sender.send(event, address);
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

	public int port() {
		return receiver.port();
	}
}
