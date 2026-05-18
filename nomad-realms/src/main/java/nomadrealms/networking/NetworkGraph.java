package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.networking.flow.NetworkFlow;
import nomadrealms.networking.flow.NetworkFlowContext;
import nomadrealms.networking.flow.NetworkFlowTemplate;
import nomadrealms.networking.flow.NetworkRole;
import nomadrealms.user.Player;

public class NetworkGraph {

	private final NetworkRole role;
	private final List<Connection> connections = new ArrayList<>();
	private final List<NetworkFlow> activeFlows = new ArrayList<>();

	public NetworkGraph(NetworkRole role) {
		this.role = role;
	}

	public void update(NetworkNode networkNode, List<Player> onlinePlayers) {
		networkNode.update((event, address) -> {
			NetworkFlowContext context = new NetworkFlowContext(networkNode, this, onlinePlayers, address);
			boolean consumed = false;
			for (NetworkFlow flow : activeFlows) {
				if (flow.consume(event, context)) {
					consumed = true;
					break;
				}
			}
			if (!consumed) {
				for (NetworkFlowTemplate template : NetworkFlowTemplate.values()) {
					Optional<NetworkFlow> flow = template.tryTrigger(event, role, address);
					if (flow.isPresent()) {
						activeFlows.add(flow.get());
						break;
					}
				}
			}
		});

		Iterator<NetworkFlow> iterator = activeFlows.iterator();
		while (iterator.hasNext()) {
			NetworkFlow flow = iterator.next();
			flow.update(new NetworkFlowContext(networkNode, this, onlinePlayers, null));
			if (flow.isCompleted()) {
				iterator.remove();
			}
		}
	}

	public void send(SyncedEvent event, PacketAddress address, NetworkNode networkNode) {
		networkNode.send(event, address);
	}

	public List<Connection> connections() {
		return connections;
	}

	public void addConnection(Connection connection) {
		connections.add(connection);
	}

	public Optional<Connection> getConnection(UUID nonce) {
		return connections.stream()
				.filter(c -> c.nonce().equals(nonce))
				.findFirst();
	}

	public Optional<Connection> getConnection(Player player) {
		return connections.stream()
				.filter(c -> c.player().name().equals(player.name()))
				.findFirst();
	}

}
