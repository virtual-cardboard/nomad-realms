package engine.context.input.networking;

import engine.context.input.networking.packet.address.PacketAddress;

public class NetworkSource {

	private final PacketAddress address;

	public NetworkSource(PacketAddress address) {
		this.address = address;
	}

	public PacketAddress address() {
		return address;
	}

	public String description() {
		return "packet from " + address.toString();
	}

}
