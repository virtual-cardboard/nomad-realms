package main;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

import process.HolePunchProcess;

public class NomadRealmsTestClient {

	public static void main(String[] args) throws IOException {
		new HolePunchProcess().start();
	}

	public static void inputControl() throws UnknownHostException, SocketException, IOException {
		InetAddress serverAddress = InetAddress.getByName("72.140.156.47");
		Scanner scanner = new Scanner(System.in);
		DatagramSocket socket = new DatagramSocket(45000);
		for (int i = 0; i < 1000; i++) {
			String nextLine = scanner.nextLine();
			if (nextLine.equalsIgnoreCase("connect")) {
				byte[] buffer = new byte[] { 0, 1, 2, 3 };
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, 45001);
				socket.send(packet);
			}
		}
		socket.close();
		scanner.close();
	}

}
