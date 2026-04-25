package nomadrealms.app;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import engine.context.input.networking.SocketFinder;
import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkingReceiver;
import engine.networking.NetworkingSender;
import nomadrealms.event.networking.PingSyncedEvent;
import nomadrealms.event.networking.PongSyncedEvent;

public class PingApp {

	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting PingApp...");
		NetworkingSender sender = new NetworkingSender();
		NetworkingReceiver receiver = new NetworkingReceiver();

		DatagramSocket socket = SocketFinder.findSocket(0);
		sender.init(socket);
		receiver.init(socket);

		PacketAddress serverAddress = new PacketAddress(InetAddress.getByName("localhost"), 44999);
		long startTime = System.currentTimeMillis();
		System.out.println("Sending PingSyncedEvent to " + serverAddress);
		sender.send(new PingSyncedEvent("Ping from PingApp", startTime), serverAddress);

		boolean receivedPong = false;
		long deadline = System.currentTimeMillis() + 5000;

		while (System.currentTimeMillis() < deadline) {
			final boolean[] pongReceived = {false};
			receiver.update((event) -> {
				if (event instanceof PongSyncedEvent) {
					PongSyncedEvent pong = (PongSyncedEvent) event;
					System.out.println("Received PongSyncedEvent: " + pong);
					System.out.println("Round trip time: " + (System.currentTimeMillis() - startTime) + "ms");
					pongReceived[0] = true;
				}
			});
			if (pongReceived[0]) {
				receivedPong = true;
				break;
			}
			Thread.sleep(100);
		}

		if (!receivedPong) {
			System.out.println("deadline exceeded");
		}

		sender.cleanUp();
		receiver.cleanUp();
		System.out.println("PingApp finished.");
	}

}
