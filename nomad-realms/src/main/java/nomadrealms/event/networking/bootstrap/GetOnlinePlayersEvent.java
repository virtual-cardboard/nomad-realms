package nomadrealms.event.networking.bootstrap;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.serialization.Derializable;
import nomadrealms.event.networking.SyncedEventHandler;

@Derializable
public class GetOnlinePlayersEvent extends BootstrapEvent {

	public GetOnlinePlayersEvent() {
	}

	@Override
	public void accept(SyncedEventHandler handler, PacketAddress address) {
		handler.resolve(this, address);
	}

}
