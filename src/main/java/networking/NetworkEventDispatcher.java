package networking;

import static app.NomadRealmsClient.SKIP_NETWORKING;

import java.util.Queue;

import context.input.networking.packet.PacketModel;
import context.input.networking.packet.address.PacketAddress;
import event.network.NomadRealmsNetworkEvent;

/**
 * {@link NetworkEventDispatcher} dispatches {@link NomadRealmsNetworkEvent}s as
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

	public void dispatch(Queue<NomadRealmsNetworkEvent> outgoingNetworkQueue) {
		while (!outgoingNetworkQueue.isEmpty()) {
			NomadRealmsNetworkEvent event = outgoingNetworkQueue.poll();
			if (SKIP_NETWORKING) {
				continue;
			}
			for (PacketAddress address : network.peers) {
				outgoingPacketModelQueue.add(event.toPacket(address));
				System.out.println("Sending " + event.getClass().getSimpleName() + " to " + address);
			}
		}
	}

}
