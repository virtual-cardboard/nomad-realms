package event.network.p2p.game;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;

public class CardHoveredNetworkEvent extends NomadRealmsP2PNetworkEvent {

	private long playerId;
	private long cardId;

	public CardHoveredNetworkEvent() {
	}

	public CardHoveredNetworkEvent(long playerId, long cardId) {
		this.playerId = playerId;
		this.cardId = cardId;
	}

	public CardHoveredNetworkEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public void read(SerializationReader reader) {
		this.playerId = reader.readLong();
		this.cardId = reader.readLong();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(playerId);
		writer.consume(cardId);
	}

	public long playerId() {
		return playerId;
	}

	public long cardId() {
		return cardId;
	}

}
