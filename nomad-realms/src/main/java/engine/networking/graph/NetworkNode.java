package engine.networking.graph;

import engine.context.input.networking.packet.address.PacketAddress;

public class NetworkNode {

	protected NetworkRole role;
	protected PacketAddress address;

	public NetworkNode(NetworkRole role, PacketAddress address) {
		this.role = role;
		this.address = address;
	}

	public NetworkRole role() {
		return role;
	}

	public PacketAddress address() {
		return address;
	}

}
