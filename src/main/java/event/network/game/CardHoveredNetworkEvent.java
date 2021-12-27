package event.network.game;

import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.NomadRealmsNetworkProtocol.CARD_HOVERED;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import event.network.NomadRealmsNetworkEvent;
import networking.protocols.NomadRealmsNetworkProtocol;

public class CardHoveredNetworkEvent extends NomadRealmsNetworkEvent {

	private long player;
	private long card;

	public CardHoveredNetworkEvent(long time, long player, long card) {
		super(time, LOCAL_HOST);
		this.player = player;
		this.card = card;
	}

	public CardHoveredNetworkEvent(NetworkSource source, PacketReader reader) {
		super(source);
		setTime(reader.readLong());
		this.player = reader.readLong();
		this.card = reader.readLong();
		reader.close();
	}

	public long player() {
		return player;
	}

	public long card() {
		return card;
	}

	@Override
	protected PacketModel toPacketModel(PacketBuilder builder) {
		return builder
				.consume(time())
				.consume(player)
				.consume(card)
				.build();
	}

	@Override
	protected NomadRealmsNetworkProtocol protocol() {
		return CARD_HOVERED;
	}

}
