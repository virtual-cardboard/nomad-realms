package event.network.p2p.game;

import static networking.protocols.NomadRealmsP2PNetworkProtocol.CARD_PLAYED_NETWORK_EVENT;

import derealizer.SerializationReader;
import derealizer.SerializationWriter;
import event.network.NomadRealmsP2PNetworkEvent;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public class CardPlayedNetworkEvent extends NomadRealmsP2PNetworkEvent {

	private long playerId;
	private long targetId;
	private long cardId;

	public CardPlayedNetworkEvent() {
	}

	public CardPlayedNetworkEvent(long playerId, long targetId, long cardId) {
		this.playerId = playerId;
		this.targetId = targetId;
		this.cardId = cardId;
	}

	public CardPlayedNetworkEvent(byte[] bytes) {
		read(new SerializationReader(bytes));
	}

	@Override
	public NomadRealmsP2PNetworkProtocol formatEnum() {
		return CARD_PLAYED_NETWORK_EVENT;
	}

	@Override
	public void read(SerializationReader reader) {
		this.playerId = reader.readLong();
		this.targetId = reader.readLong();
		this.cardId = reader.readLong();
	}

	@Override
	public void write(SerializationWriter writer) {
		writer.consume(playerId);
		writer.consume(targetId);
		writer.consume(cardId);
	}

	public long playerId() {
		return playerId;
	}

	public long targetId() {
		return targetId;
	}

	public long cardId() {
		return cardId;
	}

}
