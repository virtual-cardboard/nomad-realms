package networking.packet.destination;

import java.net.InetAddress;

public abstract class PacketDestination {

	public abstract InetAddress ip();

	public abstract int port();

}
