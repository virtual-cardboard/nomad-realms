package nomadrealms.event.networking.bootstrap;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import nomadrealms.event.networking.SyncedEventHandler;

@Derializable
public class DisconnectFromServerEvent extends BootstrapEvent {

	private String name;

	protected DisconnectFromServerEvent() {
	}

	public DisconnectFromServerEvent(String name) {
		this.name = name;
	}

	public String name() {
		return name;
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
