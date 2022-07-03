package event.network.p2p.game;

import static networking.protocols.NomadRealmsP2PNetworkProtocol.STREAM_CHUNK_DATA_EVENT;

import java.util.ArrayList;
import java.util.List;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public class StreamChunkDataEvent extends NomadRealmsP2PNetworkEvent {

	private int cx;
	private int cy;
	private List<Integer> tileTypes;

	public StreamChunkDataEvent() {
	}

	public StreamChunkDataEvent(int cx, int cy, List<Integer> tileTypes) {
		this.cx = cx;
		this.cy = cy;
		this.tileTypes = tileTypes;
	}

	public StreamChunkDataEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsP2PNetworkProtocol formatEnum() {
		return STREAM_CHUNK_DATA_EVENT;
	}

	@Override
	public void read(SerializationReader reader) {
		this.cx = reader.readInt();
		this.cy = reader.readInt();
		this.tileTypes = new ArrayList<>();
		for (byte i0 = 0, numElements0 = reader.readByte(); i0 < numElements0; i0++) {
			tileTypes.add(reader.readInt());
		}
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(cx);
		writer.consume(cy);
		writer.consume((byte) tileTypes.size());
		for (int i0 = 0; i0 < tileTypes.size(); i0++) {
			writer.consume(tileTypes.get(i0));
		}
	}

	public int cx() {
		return cx;
	}

	public int cy() {
		return cy;
	}

	public List<Integer> tileTypes() {
		return tileTypes;
	}

}
