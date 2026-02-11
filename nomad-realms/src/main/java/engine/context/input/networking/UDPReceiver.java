package engine.context.input.networking;

import static engine.context.input.networking.packet.PacketModel.toModel;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.ArrayBlockingQueue;

import engine.common.time.TerminateableRunnable;
import engine.context.input.event.PacketReceivedInputEvent;
import engine.context.input.networking.packet.address.PacketAddress;

public class UDPReceiver extends TerminateableRunnable {

	private final DatagramSocket socket;
	private final ArrayBlockingQueue<PacketReceivedInputEvent> networkReceiveBuffer;
	private final byte[] buffer = new byte[65536];
	private final DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

	public UDPReceiver(DatagramSocket socket, ArrayBlockingQueue<PacketReceivedInputEvent> networkReceiveBuffer) {
		this.socket = socket;
		this.networkReceiveBuffer = networkReceiveBuffer;
	}

	@Override
	public void doRun() {
		try {
			socket.receive(packet);
			NetworkSource source = new NetworkSource(new PacketAddress((InetSocketAddress) packet.getSocketAddress()));
			PacketReceivedInputEvent event = new PacketReceivedInputEvent(source, toModel(packet));
			networkReceiveBuffer.put(event);
		} catch (SocketTimeoutException e) {
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
