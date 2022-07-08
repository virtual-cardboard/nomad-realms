package event.network.p2p.game;

import static model.actor.ActorSerializer.deserialize;
import static networking.protocols.NomadRealmsP2PNetworkProtocol.STREAM_CHUNK_DATA_EVENT;

import java.util.ArrayList;
import java.util.List;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import model.actor.Actor;
import model.world.chunk.TileChunk;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public class StreamChunkDataEvent extends NomadRealmsP2PNetworkEvent {

	private TileChunk chunk;
	private List<Actor> actors;

	public StreamChunkDataEvent() {
	}

	public StreamChunkDataEvent(TileChunk chunk, List<Actor> actors) {
		this.chunk = chunk;
		this.actors = actors;
	}

	public StreamChunkDataEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsP2PNetworkProtocol formatEnum() {
		return STREAM_CHUNK_DATA_EVENT;
	}

	/**
	 * Non-default implementation.
	 */
	@Override
	public void read(SerializationReader reader) {
		this.chunk = new TileChunk();
		this.chunk.read(reader);
		this.actors = new ArrayList<>();
		for (byte i0 = 0, numElements0 = reader.readByte(); i0 < numElements0; i0++) {
			Actor pojo1 = deserialize(reader);
			actors.add(pojo1);
		}
		chunk.setActors(actors.stream().toArray(Actor[]::new));
	}

	/**
	 * Non-default implementation.
	 */
	@Override
	public void write(SerializationWriter writer) {
		chunk.write(writer);
		writer.consume((byte) actors.size());
		for (int i0 = 0; i0 < actors.size(); i0++) {
			actors.get(i0).writeWithId(writer);
		}
	}

	public TileChunk chunk() {
		return chunk;
	}

	public List<Actor> actors() {
		return actors;
	}

}
