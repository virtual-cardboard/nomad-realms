package process;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class HolePunchProcess {

	private static final int TEN_SECONDS = 10000;

	private boolean isDone = false;
	private final DatagramSocket socket;
	private final byte[] buffer = new byte[256];
	private final DatagramPacket message = new DatagramPacket(buffer, buffer.length);

	public HolePunchProcess() throws SocketException {
		socket = new DatagramSocket(45000);
		socket.setSoTimeout(TEN_SECONDS);
	}

	public void start() {
		System.out.println("Sending connect packet");
		sendConnect();
		System.out.println("Sent connect packet");
		System.out.println("Reading hole punch packet");
		readNext();
		System.out.println("Read hole punch packet");
		System.out.println("Expecting 6 bytes, acutal length: " + message.getLength());
		System.out.println("Expecting from 72.140.156.47, 45001, acutal from: " + message.getSocketAddress());
		System.out.println(Arrays.toString(buffer));
	}

	private void sendConnect() {
		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName("72.140.156.47");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		byte[] buffer = new byte[] { 0, 1, 2, 3 };
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, 45001);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
