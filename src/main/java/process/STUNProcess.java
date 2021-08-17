package process;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class STUNProcess {

	private static final int TEN_SECONDS = 10000;

	private boolean isDone = false;
	private final DatagramSocket socket;
	private final byte[] buffer = new byte[256];
	private final DatagramPacket message = new DatagramPacket(buffer, buffer.length);

	public STUNProcess() throws SocketException {
		socket = new DatagramSocket(44999);
		socket.setSoTimeout(TEN_SECONDS);
	}

	public void start() {
		InetAddress destIp = null;
		try {
			destIp = InetAddress.getByName("72.140.156.47");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		DatagramPacket packet1 = new DatagramPacket(buffer, buffer.length, destIp, 45001);
		DatagramPacket packet2 = new DatagramPacket(buffer, buffer.length, destIp, 45000);
		try {
			socket.send(packet1);
			socket.send(packet2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		readNext();
		System.out.println("From: " + message.getSocketAddress());
		System.out.println(Arrays.toString(buffer));
		readNext();
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
