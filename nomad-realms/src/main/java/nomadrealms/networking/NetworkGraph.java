package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.networking.flow.FlowContext;
import nomadrealms.networking.flow.NetworkFlow;
import nomadrealms.networking.flow.NetworkFlowTemplate;
import nomadrealms.networking.flow.NetworkRole;
import nomadrealms.user.Player;

public class NetworkGraph {

	private final NetworkNode networkNode = new NetworkNode();
	private final List<Connection> connections = new ArrayList<>();
	private final List<NetworkFlow> activeFlows = new ArrayList<>();
	private final List<NetworkFlow> pendingFlows = new ArrayList<>();

	public void init() {
		networkNode.init();
	}

	public void init(int port) {
		networkNode.init(port);
	}

	public int port() {
		return networkNode.port();
	}

	public void update(FlowContext context, NetworkRole role) {
		networkNode.update((event, address) -> {
			context.sourceAddress(address);
			context.triggerEvent(event);
			boolean handled = false;
			for (NetworkFlow flow : activeFlows) {
				if (flow.test(event, address)) {
					flow.handle(event, address, context);
					handled = true;
					break;
				}
			}
			if (!handled) {
				for (NetworkFlowTemplate template : NetworkFlowTemplate.values()) {
					Optional<NetworkFlow> flow = template.tryTrigger(event, address, role);
					if (flow.isPresent()) {
						NetworkFlow triggeredFlow = flow.get();
						pendingFlows.add(triggeredFlow);
						triggeredFlow.update(context);
						break;
					}
				}
			}
		});

		activeFlows.addAll(pendingFlows);
		pendingFlows.clear();

		Iterator<NetworkFlow> iterator = activeFlows.iterator();
		while (iterator.hasNext()) {
			NetworkFlow flow = iterator.next();
			if (flow.isCompleted()) {
				iterator.remove();
				continue;
			}
			flow.update(context);
			if (flow.isCompleted()) {
				iterator.remove();
			}
		}
	}

	public void send(SyncedEvent event, PacketAddress address) {
		networkNode.send(event, address);
	}

	public void cleanUp() {
		networkNode.cleanUp();
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

	public void addFlow(NetworkFlow flow) {
		activeFlows.add(flow);
	}

}
