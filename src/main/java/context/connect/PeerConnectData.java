package context.connect;

import context.data.GameData;
import context.input.networking.packet.address.PacketAddress;

public class PeerConnectData extends GameData {

	public static final int TIMEOUT_MILLISECONDS = 1000;
	public static final int RETRIES = 10;

	private boolean connected = false;
	private long lastTriedTime = -1;
	private int timesTried = 0;
	private PacketAddress peerAddress;

	public boolean isConnected() {
		return connected;
	}

	public void setConnected() {
		this.connected = true;
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

	public PacketAddress getPeerAddress() {
		return peerAddress;
	}

	public void setPeerAddress(PacketAddress peerAddress) {
		this.peerAddress = peerAddress;
	}

	public void incrementTimesTried() {
		timesTried++;
	}

}
