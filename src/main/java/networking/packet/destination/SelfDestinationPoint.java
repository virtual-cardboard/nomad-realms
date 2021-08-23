package networking.packet.destination;

import java.net.InetAddress;

public class SelfDestinationPoint extends PacketDestination {

	public static final SelfDestinationPoint SELF_DEST = new SelfDestinationPoint();

	private static InetAddress ip;
	private static int port;

	@Override
	public InetAddress ip() {
		return ip;
	}

	@Override
	public int port() {
		return port;
	}

}
