package engine.context.input.networking.packet.address.serialization;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.CustomDerializer;
import engine.serialization.Derializer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

@CustomDerializer
public class PacketAddressDerializer implements Derializer<PacketAddress> {

	public static void serialize(PacketAddress address, DataOutputStream dos) throws IOException {
		if (address == null) {
			dos.writeBoolean(false);
			return;
		}
		dos.writeBoolean(true);

		byte[] ipBytes = address.ip().getAddress();
		dos.writeInt(ipBytes.length);
		dos.write(ipBytes);

		dos.writeInt(address.port());
	}

	public static PacketAddress deserialize(DataInputStream dis) throws IOException {
		if (!dis.readBoolean()) {
			return null;
		}

		int ipLength = dis.readInt();
		byte[] ipBytes = new byte[ipLength];
		dis.readFully(ipBytes);

		int port = dis.readInt();

		InetAddress ip = InetAddress.getByAddress(ipBytes);
		return new PacketAddress(ip, port);
	}

}
