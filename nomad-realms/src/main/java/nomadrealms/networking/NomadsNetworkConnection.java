package nomadrealms.networking;

import static engine.networking.graph.ConnectionState.LISTENING;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.graph.ConnectionState;
import engine.networking.graph.NetworkConnection;
import java.util.UUID;
import nomadrealms.user.Player;

public class NomadsNetworkConnection extends NetworkConnection {

	private final Player player;
	private final UUID nonce;

	public NomadsNetworkConnection(Player player, UUID nonce) {
		super(LISTENING, player.address())
		this.player = player;
		this.nonce = nonce;
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

	public PacketAddress targetAddress() {
		return targetAddress;
	}

	public void targetAddress(PacketAddress targetAddress) {
		this.targetAddress = targetAddress;
	}

}
