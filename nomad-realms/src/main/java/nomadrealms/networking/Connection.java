package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.UUID;
import nomadrealms.user.Player;

public class Connection {

	private final Player player;
	private final UUID nonce;
	private ConnectionState state;
	private PacketAddress targetAddress;
	private long lastReceivedHeartbeat;
	private long lastSentHeartbeat;

	public Connection(Player player, UUID nonce) {
		this.player = player;
		this.nonce = nonce;
		this.state = ConnectionState.LISTENING;
		this.targetAddress = player.address();
		long currentTime = System.currentTimeMillis();
		this.lastReceivedHeartbeat = currentTime;
		this.lastSentHeartbeat = currentTime;
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
		if (this.state != ConnectionState.HEALTHY && state == ConnectionState.HEALTHY) {
			long currentTime = System.currentTimeMillis();
			this.lastReceivedHeartbeat = currentTime;
			this.lastSentHeartbeat = currentTime;
		}
		this.state = state;
	}

	public long lastReceivedHeartbeat() {
		return lastReceivedHeartbeat;
	}

	public void lastReceivedHeartbeat(long lastReceivedHeartbeat) {
		this.lastReceivedHeartbeat = lastReceivedHeartbeat;
	}

	public long lastSentHeartbeat() {
		return lastSentHeartbeat;
	}

	public void lastSentHeartbeat(long lastSentHeartbeat) {
		this.lastSentHeartbeat = lastSentHeartbeat;
	}

	public void recordHeartbeatReceived() {
		this.lastReceivedHeartbeat = System.currentTimeMillis();
		if (this.state == ConnectionState.STALE) {
			this.state = ConnectionState.HEALTHY;
		}
	}

	public PacketAddress targetAddress() {
		return targetAddress;
	}

	public void targetAddress(PacketAddress targetAddress) {
		this.targetAddress = targetAddress;
	}

}
