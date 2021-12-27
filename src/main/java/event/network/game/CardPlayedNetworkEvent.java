package event.network.game;

import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.NomadRealmsNetworkProtocol.CARD_PLAYED;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import event.network.NomadRealmsNetworkEvent;
import networking.protocols.NomadRealmsNetworkProtocol;

public class CardPlayedNetworkEvent extends NomadRealmsNetworkEvent {

	private long player;
	private long target;
	private long card;

	public CardPlayedNetworkEvent(long time, long player, long target, long card) {
		super(time, LOCAL_HOST);
		this.player = player;
		this.target = target;
		this.card = card;
	}

	/**
	 * 
	 * @param time
	 * @param player id of the card player
	 * @param card   id of the card
	 * @param target id of the target
	 */
	public CardPlayedNetworkEvent(long player, long target, long card) {
		super(LOCAL_HOST);
		this.player = player;
		this.target = target;
		this.card = card;
	}

	public CardPlayedNetworkEvent(NetworkSource source, PacketReader reader) {
		super(source);
		setTime(reader.readLong());
		this.player = reader.readLong();
		this.target = reader.readLong();
		this.card = reader.readLong();
		reader.close();
	}

	public long player() {
		return player;
	}

	public long card() {
		return card;
	}

	public long target() {
		return target;
	}

	@Override
	protected PacketModel toPacketModel(PacketBuilder builder) {
		return builder
				.consume(time())
				.consume(player)
				.consume(target)
				.consume(card)
				.build();
	}

	@Override
	protected NomadRealmsNetworkProtocol protocol() {
		return CARD_PLAYED;
	}

}
