package engine.networking.graph;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.flow.NetworkFlow;
import engine.networking.flow.template.NetworkFlowTemplate;
import engine.networking.messenger.NetworkMessenger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import nomadrealms.event.networking.SyncedEvent;

public class NetworkGraph {

	private final NetworkMessenger messenger = new NetworkMessenger();
	private final NetworkNode self;
	private final List<NetworkConnection> connections = new ArrayList<>();

	private Map<Class<? extends SyncedEvent>, NetworkFlowTemplate> templates;
	private List<NetworkFlow> activeFlows;

	public NetworkGraph(NetworkRole role) {
		self = new NetworkNode(role, null);
		messenger.init();
	}

	public void update(BiConsumer<SyncedEvent, PacketAddress> handler) {
		messenger.update(handler);
	}

	public void send(SyncedEvent event, NetworkNode node) {
		messenger.send(event, node.address);
	}

	public void cleanUp() {
		messenger.cleanUp();
	}

	public List<? extends NetworkConnection> connections() {
		return connections;
	}

	public void addConnection(NetworkConnection connection) {
		connections.add(connection);
	}

	public void registerFlow(NetworkFlowTemplate template) {
		templates.put(template.getInitialEventClass(), template);
	}

	public void initiateFlow()

}
