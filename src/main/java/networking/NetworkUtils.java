package networking;

import static context.input.networking.packet.PacketPrimitive.SHORT;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.source.NetworkSource;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.address.PacketAddress;

public class NetworkUtils {

	public static final NetworkSource LOCAL_HOST;
	public static final NetworkSource SERVER;

	static {
		InetAddress localHost = null;
		InetAddress serverDest = null;
		try {
			localHost = InetAddress.getLocalHost();
			serverDest = InetAddress.getByName("72.140.156.47");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		LOCAL_HOST = new NetworkSource(new PacketAddress(localHost, 45000));
		SERVER = new NetworkSource(new PacketAddress(serverDest, 45000));
	}

	/**
	 * protocol_id
	 */
	public static final PacketFormat PROTOCOL_ID = new PacketFormat().with(SHORT);

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
