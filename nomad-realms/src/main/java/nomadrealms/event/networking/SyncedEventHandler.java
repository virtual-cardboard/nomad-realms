package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InteractEvent;
import nomadrealms.event.networking.bootstrap.BootstrapEvent;

public interface SyncedEventHandler {

	void resolve(SyncedEvent event, PacketAddress address);
	void resolve(PingSyncedEvent event, PacketAddress address);
	void resolve(PongSyncedEvent event, PacketAddress address);
	void resolve(BootstrapEvent event, PacketAddress address);
	void resolve(InputEvent event, PacketAddress address);
	void resolve(DropItemEvent event, PacketAddress address);
	void resolve(CardPlayedEvent event, PacketAddress address);
	void resolve(InteractEvent event, PacketAddress address);

}
