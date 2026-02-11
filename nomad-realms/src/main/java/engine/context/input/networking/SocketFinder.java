package engine.context.input.networking;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

public class SocketFinder {

	public static DatagramSocket findSocket(Integer targetPort) throws IOException {
		DatagramSocket socket = null;
		if (targetPort == null) {
			targetPort = 44999;
			while (socket == null) {
				if (targetPort < 8080) {
					throw new IOException("All ports in range 8081-44999 in use");
				}
				try {
					socket = new DatagramSocket(targetPort);
				} catch (SocketException ignored) {
				}
				targetPort--;
			}
		} else {
			try {
				socket = new DatagramSocket(targetPort);
			} catch (SocketException e) {
				throw new IOException("Port " + targetPort + " in use");
			}
		}
		socket.setSoTimeout(10000);
		return socket;
	}

}
