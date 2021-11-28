package event.network;

import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.ProtocolID.CARD_PLAYED;

import java.io.Serializable;

import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import networking.protocols.ProtocolID;

public class CardPlayedNetworkEvent extends NomadRealmsNetworkEvent implements Serializable {

	private static final long serialVersionUID = -4672057528230548804L;

	private long player;
	private long card;
	private long target;

	public CardPlayedNetworkEvent(long time, long player, long target, long card) {
		super(time, LOCAL_HOST);
		this.player = player;
		this.card = card;
		this.target = target;
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
		this.card = card;
		this.target = target;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ProtocolID protocolID() {
		return CARD_PLAYED;
	}

}
