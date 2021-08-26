package context;

import static context.input.networking.packet.address.STUNAddress.STUN_ADDRESS;

import java.util.List;

import common.GameInputEventHandler;
import common.event.GameEvent;
import common.source.NetworkSource;
import context.input.GameInput;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.address.PacketAddress;
import context.input.networking.packet.block.PacketBlock;
import event.STUNResponseEvent;
import protocol.STUNProtocol;

public class STUNInput extends GameInput {

	public STUNInput() {
		addPacketReceivedFunction(new GameInputEventHandler<>((packet) -> {
			NetworkSource source = (NetworkSource) packet.getSource();
			if (source.getAddress() == STUN_ADDRESS) {
				PacketAddress address = parseResponseForAddress(packet.getModel());
				return new STUNResponseEvent(System.currentTimeMillis(), source, address);
			}
			System.out.println("hi");
			return new GameEvent(0, null) {
			};
		}));
	}

	private PacketAddress parseResponseForAddress(PacketModel packetModel) {
		List<PacketBlock> blocks = packetModel.blocks();
		if (blocks.size() != 1) {
			throw new RuntimeException("Expected block of length 1");
		}
		STUNProtocol.STUN_RESPONSE.reader(null);
		return null;
	}

}
