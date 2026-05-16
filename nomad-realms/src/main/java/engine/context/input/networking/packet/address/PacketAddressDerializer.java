package engine.context.input.networking.packet.address;

import static engine.serialization.DerializableHelper.readByte;
import static engine.serialization.DerializableHelper.readShort;
import static engine.serialization.DerializableHelper.write;

import engine.serialization.CustomDerializer;
import engine.serialization.Derializer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;

@CustomDerializer
public class PacketAddressDerializer implements Derializer<PacketAddress> {

	public static void serialize(PacketAddress address, DataOutputStream dos) throws IOException {
		byte[] ip = address.ip().getAddress();
		for (byte b : ip) {
			write(b, dos);
		}
		write((short) address.port(), dos);
	}

	public static PacketAddress deserialize(DataInputStream dis) throws IOException {
		byte[] ip = new byte[4];
		for (int i = 0; i < 4; i++) {
			ip[i] = readByte(dis);
		}
		short port = readShort(dis);
		return new PacketAddress(InetAddress.getByAddress(ip), port & 0xFFFF);
	}

}
