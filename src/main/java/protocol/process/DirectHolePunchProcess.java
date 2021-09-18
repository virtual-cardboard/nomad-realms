package protocol.process;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class DirectHolePunchProcess {

	private boolean isDone = false;
	private final DatagramSocket socket;
	private final byte[] buffer = new byte[256];
	private final DatagramPacket message = new DatagramPacket(buffer, buffer.length);

	public DirectHolePunchProcess(DatagramSocket socket) {
		this.socket = socket;
	}

	public void start() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			byte[] buffer = new byte[] { 123 };
			InetAddress destIp = InetAddress.getByName("72.140.156.47");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, destIp, 45001);
			System.out.println("Sending peer hole punch packet");
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		readNext();
		System.out.println("Received peer hole punch packet");
		System.out.println("From: " + message.getSocketAddress());
		System.out.println(Arrays.toString(buffer));
	}

	public void readNext() {
		while (!isDone) {
			try {
				socket.receive(message);
			} catch (SocketTimeoutException e) {
				System.out.println("Receive timed out");
				continue;
			} catch (IOException e) {
				System.out.println("Error when receiving packet");
				e.printStackTrace();
				continue;
			}
			break;
		}
	}

}
