package event.network;

import static networking.NetworkUtils.LOCAL_HOST;
import static networking.protocols.ProtocolID.CARD_HOVERED;

import java.io.Serializable;

import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import networking.protocols.ProtocolID;

public class CardHoveredNetworkEvent extends NomadRealmsNetworkEvent implements Serializable {

	private static final long serialVersionUID = -4672057528230548804L;

	private long player;
	private long card;

	public CardHoveredNetworkEvent(long time, long player, long card) {
		super(time, LOCAL_HOST);
		this.player = player;
		this.card = card;
	}

	public long player() {
		return player;
	}

	public long card() {
		return card;
	}

	@Override
	protected PacketModel toPacketModel(PacketBuilder builder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ProtocolID protocolID() {
		return CARD_HOVERED;
	}

}
