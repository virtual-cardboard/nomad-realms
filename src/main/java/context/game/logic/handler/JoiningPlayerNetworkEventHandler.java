package context.game.logic.handler;

import static model.world.chunk.AbstractTileChunk.chunkPos;
import static model.world.tile.Tile.tileCoords;

import java.util.Queue;
import java.util.function.Consumer;

import context.game.NomadsGameData;
import engine.common.event.async.AsyncEventPriorityQueue;
import engine.common.networking.packet.PacketModel;
import event.logicprocessing.SpawnPlayerAsyncEvent;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.s2c.JoiningPlayerNetworkEvent;
import math.WorldPos;

public class JoiningPlayerNetworkEventHandler implements Consumer<JoiningPlayerNetworkEvent> {

	private final NomadsGameData data;
	private final AsyncEventPriorityQueue asyncEventPriorityQueue;
	private final Queue<PacketModel> networkSend;

	public JoiningPlayerNetworkEventHandler(NomadsGameData data, AsyncEventPriorityQueue asyncEventPriorityQueue, Queue<PacketModel> networkSend) {
		this.data = data;
		this.asyncEventPriorityQueue = asyncEventPriorityQueue;
		this.networkSend = networkSend;
	}

	@Override
	public void accept(JoiningPlayerNetworkEvent e) {
		System.out.println("Received JoiningPlayerNetworkEvent: " + e.nonce() + " " + e.lanAddress() + " " + e.wanAddress());
		PeerConnectRequestEvent connectRequest = new PeerConnectRequestEvent(e.nonce(), data.username());
		System.out.println("Sending PeerConnectRequestEvent to the joining player");
		networkSend.add(connectRequest.toPacketModel(e.lanAddress()));
		networkSend.add(connectRequest.toPacketModel(e.wanAddress()));
		System.out.println("Scheduled to spawn player on tick " + (e.spawnTick()));
		WorldPos spawnPos = new WorldPos(chunkPos(e.spawnPos()), tileCoords(e.spawnPos()));
		asyncEventPriorityQueue.add(new SpawnPlayerAsyncEvent(e.spawnTick(), spawnPos));
	}

}
