package networking.protocols;

import java.util.Arrays;
import java.util.function.Function;

import context.input.event.PacketReceivedInputEvent;
import derealizer.Derealizer;
import engine.common.event.GameEvent;
import event.network.NomadRealmsP2PNetworkEvent;

public class NomadRealmsProtocolDecoder implements Function<PacketReceivedInputEvent, GameEvent> {

	@Override
	public NomadRealmsP2PNetworkEvent apply(PacketReceivedInputEvent event) {
		byte[] bytes = event.model().bytes();
		try {
			return (NomadRealmsP2PNetworkEvent) Derealizer.deserialize(bytes, NomadRealmsP2PNetworkEventEnum.class);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Could not decode bytes to NomadRealmsP2PNetworkEvent: " + Arrays.toString(bytes));
			return null;
		}
	}

}
