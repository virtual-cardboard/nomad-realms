package engine.context.input.networking;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Queue;

import engine.common.time.TerminateableRunnable;
import engine.context.input.networking.packet.PacketModel;

public class UDPSender extends TerminateableRunnable {

	private final DatagramSocket socket;
	private final Queue<PacketModel> networkQueue;

	public UDPSender(DatagramSocket socket, Queue<PacketModel> networkQueue) {
		this.socket = socket;
		this.networkQueue = networkQueue;
	}

	public void sendAsync(PacketModel packet) {
		networkQueue.add(packet);
	}

	@Override
	public void doRun() {
		while (!networkQueue.isEmpty()) {
			try {
				socket.send(PacketModel.toPacket(networkQueue.poll()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
