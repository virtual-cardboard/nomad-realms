package main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

import process.STUNProcess;

public class NomadRealmsTestClient {

	public static void main(String[] args) {
		DatagramSocket socket;
		try {
			socket = initializeSocket();
			new STUNProcess(socket).start();
//			new HolePunchProcess(socket).start();
//			new DirectHolePunchProcess(socket).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static DatagramSocket initializeSocket() throws IOException {
		DatagramSocket socket = null;
		int port = 44999;
		while (socket == null) {
			if (port < 8080) {
				throw new IOException("All ports in range 8081-44999 in use");
			}
			try {
				socket = new DatagramSocket(port);
				socket.setSoTimeout(10000);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			port--;
		}
		System.out.println("Started client on port " + port);
		return socket;
	}

}
