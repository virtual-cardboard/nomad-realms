package event.network;

import static context.input.networking.packet.PacketPrimitive.LONG;
import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.ProtocolID.CARD_PLAYED;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketFormat;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.PacketReader;
import networking.protocols.ProtocolID;

public class CardPlayedNetworkEvent extends NomadRealmsNetworkEvent {

	/**
	 * protocol_id(150): timestamp, player_id, target_id, card_id
	 */
	public static final PacketFormat CARD_PLAYED_FORMAT = new PacketFormat().with(LONG, LONG, LONG, LONG);

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

	public CardPlayedNetworkEvent(NetworkSource source, PacketReader protocolReader) {
		super(source);
		PacketReader reader = CARD_PLAYED_FORMAT.reader(protocolReader);
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
		return CARD_PLAYED_FORMAT.builder(builder)
				.consume(time())
				.consume(player)
				.consume(target)
				.consume(card)
				.build();
	}

	@Override
	protected ProtocolID protocolID() {
		return CARD_PLAYED;
	}

}
