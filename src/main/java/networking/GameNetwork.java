package networking;

import java.util.ArrayList;
import java.util.List;

import context.input.networking.packet.address.PacketAddress;

public class GameNetwork {

	protected List<PacketAddress> peers = new ArrayList<>();
	protected List<PacketAddress> backups = new ArrayList<>();

	public GameNetwork() {
	}

	public void addPeer(PacketAddress address) {
		peers.add(address);
	}

	public void addBackup(PacketAddress address) {
		backups.add(address);
	}

}
