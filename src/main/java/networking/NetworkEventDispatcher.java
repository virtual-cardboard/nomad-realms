package networking;

import java.util.Queue;

import common.event.GameEvent;
import context.input.networking.packet.PacketModel;

public class NetworkEventDispatcher {

	private GameNetwork network;
	private Queue<PacketModel> networkQueue;

	public NetworkEventDispatcher(GameNetwork network, Queue<PacketModel> networkQueue) {
		this.network = network;
		this.networkQueue = networkQueue;
	}

	public NetworkEventDispatcher(Queue<PacketModel> networkQueue) {
		this(new GameNetwork(), networkQueue);
	}

	public void dispatch(Queue<GameEvent> sync) {
		while (!sync.isEmpty()) {
			GameEvent event = sync.poll();
//			if (event instanceof NomadRealmsNetworkEvent) {
//				NomadRealmsNetworkEvent networkEvent = (NomadRealmsNetworkEvent) event;
//				for (PacketAddress address : network.peers) {
//					networkQueue.add(networkEvent.toPacket(address));
//				}
//			}
		}
	}

}
