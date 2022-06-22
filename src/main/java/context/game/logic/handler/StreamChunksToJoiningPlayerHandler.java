package context.game.logic.handler;

import static context.game.visuals.GameCamera.RENDER_RADIUS;
import static model.world.chunk.AbstractTileChunk.chunkPos;
import static model.world.tile.Tile.tileCoords;

import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import context.game.NomadsGameData;
import engine.common.math.Vector2i;
import engine.common.networking.packet.PacketModel;
import event.network.p2p.game.StreamChunkDataEvent;
import event.network.p2p.s2c.JoiningPlayerNetworkEvent;
import math.WorldPos;
import model.world.WorldMap;
import model.world.chunk.TileChunk;

public class StreamChunksToJoiningPlayerHandler implements Consumer<JoiningPlayerNetworkEvent> {

	private final NomadsGameData data;
	private final Queue<PacketModel> networkSend;

	public StreamChunksToJoiningPlayerHandler(NomadsGameData data, Queue<PacketModel> networkSend) {
		this.data = data;
		this.networkSend = networkSend;
	}

	@Override
	public void accept(JoiningPlayerNetworkEvent e) {
		WorldPos spawnPos = new WorldPos(chunkPos(e.spawnPos()), tileCoords(e.spawnPos()));
		Vector2i spawnChunkPos = spawnPos.chunkPos();
		WorldMap worldMap = data.previousState().worldMap();

		int streamRadius = RENDER_RADIUS + 1; // TODO figure out the radius of chunks to stream

		for (int y = -streamRadius; y <= streamRadius; y++) {
			for (int x = -streamRadius; x <= streamRadius; x++) {
				Vector2i chunkPos = spawnChunkPos.add(x, y);
				TileChunk chunk = worldMap.chunk(chunkPos);
				if (chunk != null) {
					List<Integer> tileTypes = chunk.getTilesAsList().stream()
							.map(t -> t.type().ordinal())
							.collect(Collectors.toList());
					StreamChunkDataEvent event = new StreamChunkDataEvent(chunkPos.x, chunkPos.y, tileTypes);
					networkSend.add(event.toPacketModel(e.lanAddress()));
				}
			}
		}
	}

}
