package protocol;

import static context.input.networking.packet.PacketPrimitive.BYTE;

import java.net.InetAddress;
import java.net.UnknownHostException;

import context.input.networking.packet.PacketFormat;

public class NetworkUtils {

	/**
	 * protocol_id
	 */
	public static final PacketFormat PROTOCOL_FORMAT = new PacketFormat().with(BYTE);

	public static InetAddress addressFromBytes(byte[] bytes) {
		InetAddress ip = null;
		try {
			ip = InetAddress.getByAddress(bytes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;
	}

}
