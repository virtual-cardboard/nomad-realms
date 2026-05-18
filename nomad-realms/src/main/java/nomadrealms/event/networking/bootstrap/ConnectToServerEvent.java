package nomadrealms.event.networking.bootstrap;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;

@Derializable
public class ConnectToServerEvent extends BootstrapEvent {

	private String name;

	protected ConnectToServerEvent() {
	}

	public ConnectToServerEvent(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}



}
