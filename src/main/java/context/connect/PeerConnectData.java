package context.connect;

import static app.NomadRealmsClient.SKIP_NETWORKING;

import java.util.ArrayList;
import java.util.List;

import context.data.GameData;
import engine.common.networking.packet.address.PacketAddress;
import event.network.c2s.JoinClusterResponseEvent;

public class PeerConnectData extends GameData {

	public static final int TIMEOUT_MILLISECONDS = 1000;
	public static final int MAX_RETRIES = 1000;

	private String username;

	private volatile boolean connected = SKIP_NETWORKING;

	private List<PacketAddress> lanAddresses;
	private List<PacketAddress> wanAddresses;
	private long nonce;

	private List<PacketAddress> connectedPeers = new ArrayList<>();

	private long lastTriedTime = -1;
	private int timesTried = 0;

	public PeerConnectData(JoinClusterResponseEvent response, String username) {
		this.lanAddresses = response.lanAddresses();
		this.wanAddresses = response.wanAddresses();
		this.nonce = response.nonce();
		this.username = username;
	}

	public void confirmConnectedPeer(PacketAddress address) {
		connectedPeers.add(address);
	}

	public String username() {
		return username;
	}

	public boolean isConnected() {
		return connected;
	}

	public long lastTriedTime() {
		return lastTriedTime;
	}

	public void setLastTriedTime(long lastTriedTime) {
		this.lastTriedTime = lastTriedTime;
	}

	public int timesTried() {
		return timesTried;
	}

	public void incrementTimesTried() {
		timesTried++;
	}

	public List<PacketAddress> lanAddresses() {
		return lanAddresses;
	}

	public List<PacketAddress> wanAddresses() {
		return wanAddresses;
	}

	public long nonce() {
		return nonce;
	}

}
