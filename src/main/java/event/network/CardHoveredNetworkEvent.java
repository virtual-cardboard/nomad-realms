package event.network;

import static context.input.networking.packet.PacketPrimitive.LONG;
import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.ProtocolID.CARD_HOVERED;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import networking.protocols.ProtocolID;

public class CardHoveredNetworkEvent extends NomadRealmsNetworkEvent {

	/**
	 * protocol_id(150): timestamp, player_id, card_id
	 */
	public static final PacketFormat CARD_HOVERED_FORMAT = new PacketFormat().with(LONG, LONG, LONG);

	private long player;
	private long card;

	public CardHoveredNetworkEvent(long time, long player, long card) {
		super(time, LOCAL_HOST);
		this.player = player;
		this.card = card;
	}

	public CardHoveredNetworkEvent(NetworkSource source, PacketReader protocolReader) {
		super(source);
		PacketReader reader = CARD_HOVERED_FORMAT.reader(protocolReader);
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
		return CARD_HOVERED_FORMAT.builder(builder)
				.consume(time())
				.consume(player)
				.consume(card)
				.build();
	}

	@Override
	protected ProtocolID protocolID() {
		return CARD_HOVERED;
	}

}
