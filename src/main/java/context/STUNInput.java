package context;

import static address.STUNAddress.STUN_ADDRESS;
import static address.ServerAddress.SERVER_ADDRESS;
import static java.lang.System.currentTimeMillis;
import static protocol.STUNProtocol.STUN_RESPONSE;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.GameInputEventHandler;
import common.event.GameEvent;
import common.source.NetworkSource;
import context.input.GameInput;
import context.input.networking.packet.PacketReader;
import context.input.networking.packet.address.PacketAddress;
import context.input.networking.packet.address.PeerAddress;
import event.STUNResponseEvent;

public class STUNInput extends GameInput {

	public STUNInput() {
		addPacketReceivedFunction(new GameInputEventHandler<>((event) -> {
			NetworkSource source = (NetworkSource) event.getSource();
			if (STUN_ADDRESS.equals(source.getAddress()) || SERVER_ADDRESS.equals(source.getAddress())) {
				PacketReader reader = STUN_RESPONSE.reader(event.getModel());
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
				PacketAddress address = new PeerAddress(ip, port);
				System.out.println("Reported by: " + source.getAddress());
				return new STUNResponseEvent(currentTimeMillis(), source, timestamp, nonce, address);
			}
			return new GameEvent(0, null) {
			};
		}));
	}

}
