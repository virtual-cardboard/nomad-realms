package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.UUID;
import nomadrealms.user.Player;

public class Connection {

	private final Player player;
	private final UUID nonce;
	private ConnectionState state;
	private int ackFramesLeft;
	private PacketAddress targetAddress;

	public Connection(Player player, UUID nonce) {
		this.player = player;
		this.nonce = nonce;
		this.state = ConnectionState.LISTENING;
		this.targetAddress = player.address();
	}

	public Player player() {
		return player;
	}

	public UUID nonce() {
		return nonce;
	}

	public ConnectionState state() {
		return state;
	}

	public void state(ConnectionState state) {
		this.state = state;
	}

	public int ackFramesLeft() {
		return ackFramesLeft;
	}

	public void ackFramesLeft(int ackFramesLeft) {
		this.ackFramesLeft = ackFramesLeft;
	}

	public PacketAddress targetAddress() {
		return targetAddress;
	}

	public void targetAddress(PacketAddress targetAddress) {
		this.targetAddress = targetAddress;
	}

}
