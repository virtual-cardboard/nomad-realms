package networking;

import static app.NomadRealmsClient.SKIP_NETWORKING;

import java.util.Queue;

import context.input.networking.packet.PacketModel;
import context.input.networking.packet.address.PacketAddress;
import event.network.NomadRealmsP2PNetworkEvent;

/**
 * {@link NetworkEventDispatcher} dispatches {@link NomadRealmsP2PNetworkEvent}s as
 * packets to the correct addresses in the {@link GameNetwork}.
 *
 * @author Jay
 */
public class NetworkEventDispatcher {

	private GameNetwork network;
	private Queue<PacketModel> outgoingPacketModelQueue;

	public NetworkEventDispatcher(GameNetwork network, Queue<PacketModel> outgoingPacketModelQueue) {
		this.network = network;
		this.outgoingPacketModelQueue = outgoingPacketModelQueue;
	}

	public NetworkEventDispatcher(Queue<PacketModel> networkQueue) {
		this(new GameNetwork(), networkQueue);
	}

	public void dispatch(Queue<NomadRealmsP2PNetworkEvent> outgoingNetworkQueue) {
		while (!outgoingNetworkQueue.isEmpty()) {
			NomadRealmsP2PNetworkEvent event = outgoingNetworkQueue.poll();
			if (SKIP_NETWORKING) {
				continue;
			}
			for (PacketAddress address : network.peers) {
				outgoingPacketModelQueue.add(event.toPacketModel(address));
				System.out.println("Sending " + event.getClass().getSimpleName() + " to " + address);
			}
		}
	}

}
