package address;

import java.net.InetAddress;
import java.net.UnknownHostException;

import context.input.networking.packet.address.PacketAddress;

public class ServerAddress extends PacketAddress {

	public static final ServerAddress SERVER_ADDRESS = new ServerAddress();

	private static final InetAddress SERVER_IP;
	private static final int SERVER_PORT = 45000;

	static {
		InetAddress serverDest = null;
		try {
			serverDest = InetAddress.getByName("72.140.156.47");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		SERVER_IP = serverDest;
	}

	@Override
	public InetAddress ip() {
		return SERVER_IP;
	}

	@Override
	public int port() {
		return SERVER_PORT;
	}

}
