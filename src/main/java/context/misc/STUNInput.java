package context.misc;

import static java.lang.System.currentTimeMillis;
import static protocol.STUNProtocol.STUN_RESPONSE;
import static protocol.address.STUNAddress.STUN_ADDRESS;
import static protocol.address.ServerAddress.SERVER_ADDRESS;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.source.NetworkSource;
import context.input.GameInput;
import context.input.networking.packet.PacketReader;
import context.input.networking.packet.address.PacketAddress;
import event.connect.STUNResponseEvent;

public class STUNInput extends GameInput {

	public STUNInput() {
		addPacketReceivedFunction((event) -> {
			NetworkSource source = (NetworkSource) event.source();
			if (STUN_ADDRESS.equals(source.getAddress()) || SERVER_ADDRESS.equals(source.getAddress())) {
				PacketReader reader = STUN_RESPONSE.reader(event.model());
				long timestamp = reader.readLong();
				long nonce = reader.readLong();
				System.out.println("Nonce: " + nonce);
				InetAddress ip;
				try {
					ip = InetAddress.getByAddress(reader.readIPv4());
				} catch (UnknownHostException e) {
					e.printStackTrace();
					return null;
				}
				System.out.println("My WAN ip: " + ip);
				short port = reader.readShort();
				System.out.println("My WAN port: " + port);
				PacketAddress address = new PacketAddress(ip, port);
				System.out.println("Reported by: " + source.getAddress());
				return new STUNResponseEvent(currentTimeMillis(), source, timestamp, nonce, address);
			}
			return null;
		});
	}

}
