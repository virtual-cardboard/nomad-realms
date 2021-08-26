package context;

import static context.input.networking.packet.address.STUNAddress.STUN_ADDRESS;
import static context.input.networking.packet.address.ServerAddress.SERVER_ADDRESS;
import static protocol.STUNProtocol.STUN_RESPONSE;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import common.GameInputEventHandler;
import common.event.GameEvent;
import common.source.NetworkSource;
import context.input.GameInput;
import context.input.networking.packet.address.PacketAddress;
import context.input.networking.packet.address.PeerAddress;
import context.input.networking.packet.block.PacketBlock;
import context.input.networking.packet.block.PacketBlockReader;
import event.STUNResponseEvent;

public class STUNInput extends GameInput {

	public STUNInput() {
		addPacketReceivedFunction(new GameInputEventHandler<>((packet) -> {
			NetworkSource source = (NetworkSource) packet.getSource();
			if (source.getAddress() == STUN_ADDRESS || source.getAddress() == SERVER_ADDRESS) {
				List<PacketBlock> blocks = packet.getModel().blocks();
				if (blocks.size() != 1) {
					throw new RuntimeException("Expected block of length 1");
				}
				PacketBlockReader reader = STUN_RESPONSE.reader(blocks.get(0));
				long timestamp = reader.readLong();
				long nonce = reader.readLong();
				InetAddress ip;
				try {
					ip = InetAddress.getByAddress(reader.readIPv4());
				} catch (UnknownHostException e) {
					e.printStackTrace();
					return null;
				}
				short port = reader.readShort();
				PacketAddress address = new PeerAddress(ip, port);
				return new STUNResponseEvent(System.currentTimeMillis(), source, timestamp, nonce, address);
			}
			System.out.println("hi");
			return new GameEvent(0, null) {
			};
		}));
	}

}
