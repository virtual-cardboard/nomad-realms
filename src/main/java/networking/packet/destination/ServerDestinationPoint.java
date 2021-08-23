package networking.packet.destination;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerDestinationPoint extends PacketDestination {

	private static final InetAddress SERVER_DEST;
	private static final int SERVER_PORT = 45000;

	static {
		InetAddress serverDest = null;
		try {
			serverDest = InetAddress.getByName("72.140.156.47");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		SERVER_DEST = serverDest;
	}

	@Override
	public InetAddress ip() {
		return SERVER_DEST;
	}

	@Override
	public int port() {
		return SERVER_PORT;
	}

}
