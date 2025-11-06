package engine.context.input.networking.packet.address;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class PacketAddress {

	private InetAddress ip;
	private short port;

	public PacketAddress() {
	}

	public PacketAddress(InetAddress ip, int port) {
		this.ip = ip;
		this.port = (short) port;
	}

	public PacketAddress(InetSocketAddress address) {
		this(address.getAddress(), (short) address.getPort());
	}

//	public PacketAddress(byte[] bytes) {
//		read(new SerializationReader(bytes));
//	}
//
//	@Override
//	public void read(SerializationReader reader) {
//		try {
//			this.ip = InetAddress.getByAddress(new byte[] { reader.readByte(), reader.readByte(), reader.readByte(), reader.readByte() });
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		this.port = reader.readShort();
//	}
//
//	@Override
//	public void write(SerializationWriter writer) {
//		for (byte b : ip.getAddress()) {
//			writer.consume(b);
//		}
//		writer.consume(port);
//	}

	public InetAddress ip() {
		return ip;
	}

	public int port() {
		return port & 0xFFFF;
	}

	@Override
	public String toString() {
		return ip().toString() + ":" + port();
	}

	public static boolean match(PacketAddress a1, PacketAddress a2) {
		return a1.port() == a2.port() && a1.ip().equals(a2.ip());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof PacketAddress) {
			PacketAddress other = (PacketAddress) obj;
			return port() == other.port() && ip().equals(other.ip());
		}
		return false;
	}

}
