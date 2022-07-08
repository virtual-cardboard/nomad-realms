package networking;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import engine.common.networking.packet.address.PacketAddress;
import engine.common.source.NetworkSource;

public class ClientNetworkUtils {

	private static final String SERVER_IP = "localhost";
	public static final NetworkSource LOCAL_HOST;
	public static final NetworkSource SERVER;
	public static final String SERVER_HTTP_URL = "http://" + SERVER_IP + ":45001";

	static {
		InetAddress localHost = null;
		InetAddress serverDest = null;
		try (final DatagramSocket socket = new DatagramSocket()) {
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			localHost = socket.getLocalAddress();
			serverDest = InetAddress.getByName(SERVER_IP);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
		LOCAL_HOST = new NetworkSource(new PacketAddress(localHost, 44000));
		SERVER = new NetworkSource(new PacketAddress(serverDest, 45000));
	}

	public static InetAddress toIP(byte[] bytes) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getByAddress(bytes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

	public static PacketAddress toIP(byte[] bytes, short port) {
		return new PacketAddress(toIP(bytes), port & 0xFFFF);
	}

}
