package nomadrealms.networking.flow;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.util.List;
import nomadrealms.networking.NetworkGraph;
import nomadrealms.user.Player;

public class NetworkFlowContext {

	private final NetworkNode networkNode;
	private final NetworkGraph networkGraph;
	private final List<Player> onlinePlayers;
	private final PacketAddress source;

	public NetworkFlowContext(NetworkNode networkNode, NetworkGraph networkGraph, List<Player> onlinePlayers, PacketAddress source) {
		this.networkNode = networkNode;
		this.networkGraph = networkGraph;
		this.onlinePlayers = onlinePlayers;
		this.source = source;
	}

	public NetworkNode networkNode() {
		return networkNode;
	}

	public NetworkGraph networkGraph() {
		return networkGraph;
	}

	public List<Player> onlinePlayers() {
		return onlinePlayers;
	}

	public PacketAddress source() {
		return source;
	}

}
