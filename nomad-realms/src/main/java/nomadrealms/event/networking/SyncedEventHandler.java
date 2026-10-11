package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import nomadrealms.context.game.event.CardPlayedEvent;
import nomadrealms.context.game.event.DropItemEvent;
import nomadrealms.context.game.event.InputEvent;
import nomadrealms.context.game.event.InteractEvent;
import nomadrealms.event.networking.bootstrap.BootstrapEvent;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.DisconnectFromServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.bootstrap.OnlinePlayersListEvent;

public interface SyncedEventHandler {

	default void handle(SyncedEvent event, PacketAddress address) {
		event.accept(this, address);
	}

	void resolve(SyncedEvent event, PacketAddress address);

	void resolve(PingSyncedEvent event, PacketAddress address);

	void resolve(PongSyncedEvent event, PacketAddress address);

	void resolve(BootstrapEvent event, PacketAddress address);

	void resolve(ConnectToServerEvent event, PacketAddress address);

	void resolve(DisconnectFromServerEvent event, PacketAddress address);

	void resolve(GetOnlinePlayersEvent event, PacketAddress address);

	void resolve(OnlinePlayersListEvent event, PacketAddress address);

	void resolve(InputEvent event, PacketAddress address);

	void resolve(DropItemEvent event, PacketAddress address);

	void resolve(CardPlayedEvent event, PacketAddress address);

	void resolve(InteractEvent event, PacketAddress address);

	void resolve(HolePunchInitiationEvent event, PacketAddress address);

	void resolve(HolePunchInitiationInfoPackageEvent event, PacketAddress address);

	void resolve(HolePunchInitiationIntentEvent event, PacketAddress address);

	void resolve(HolePunchEvent event, PacketAddress address);

	void resolve(HolePunchSuccessConfirmationEvent event, PacketAddress address);

	void resolve(HolePunchSuccessAcknowledgementEvent event, PacketAddress address);

}
