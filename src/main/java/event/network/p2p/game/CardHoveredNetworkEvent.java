package event.network.p2p.game;

import static networking.protocols.NomadRealmsP2PNetworkProtocol.CARD_HOVERED_NETWORK_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

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
	public NomadRealmsP2PNetworkProtocol formatEnum() {
		return CARD_HOVERED_NETWORK_EVENT;
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
