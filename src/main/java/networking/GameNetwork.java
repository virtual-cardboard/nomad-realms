package networking;

import java.util.ArrayList;
import java.util.List;

import engine.common.networking.packet.address.PacketAddress;

public class GameNetwork {

	protected List<PacketAddress> peers = new ArrayList<>();
	protected List<PacketAddress> backups = new ArrayList<>();

	public List<PacketAddress> peers() {
		return peers;
	}

	public void addPeer(PacketAddress address) {
		peers.add(address);
	}

	public void addBackup(PacketAddress address) {
		backups.add(address);
	}

}
