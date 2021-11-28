package context.connect;

import context.data.GameData;
import context.input.networking.packet.address.PacketAddress;

public class PeerConnectData extends GameData {

	public static final int TIMEOUT_MILLISECONDS = 1000;
	public static final int MAX_RETRIES = 1000;

//	private static int WAN_CONNECTED = 0;
//	private static int LAN_CONNECTED = 1;

	private boolean connected = true;
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
