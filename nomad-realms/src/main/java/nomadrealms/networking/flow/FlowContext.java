package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import java.util.List;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.networking.NetworkGraph;
import nomadrealms.user.Player;

public class FlowContext {

	private final NetworkGraph networkGraph;
	private final List<Player> onlinePlayers;
	private final Player localPlayer;
	private PacketAddress sourceAddress;
	private SyncedEvent triggerEvent;

	public FlowContext(NetworkGraph networkGraph, List<Player> onlinePlayers, Player localPlayer) {
		this.networkGraph = networkGraph;
		this.onlinePlayers = onlinePlayers;
		this.localPlayer = localPlayer;
	}

	public NetworkGraph networkGraph() {
		return networkGraph;
	}

	public List<Player> onlinePlayers() {
		return onlinePlayers;
	}

	public Player localPlayer() {
		return localPlayer;
	}

	public PacketAddress sourceAddress() {
		return sourceAddress;
	}

	public void sourceAddress(PacketAddress sourceAddress) {
		this.sourceAddress = sourceAddress;
	}

	public SyncedEvent triggerEvent() {
		return triggerEvent;
	}

	public void triggerEvent(SyncedEvent triggerEvent) {
		this.triggerEvent = triggerEvent;
	}

}
