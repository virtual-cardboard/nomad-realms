package nomadrealms.networking.flow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import nomadrealms.event.networking.SyncedEvent;

public class NetworkFlowBuilder {

	private final List<NetworkFlowTemplateNode> nodes = new ArrayList<>();

	private NetworkFlowBuilder(NetworkFlowTemplateNode node) {
		nodes.add(node);
	}

	public static NetworkFlowBuilder expect(Class<? extends SyncedEvent> eventClass) {
		return new NetworkFlowBuilder(new ExpectNode(eventClass));
	}

	public static NetworkFlowBuilder send(SyncedEvent event) {
		return new NetworkFlowBuilder(new SendNode(event));
	}

	public static NetworkFlowBuilder onRole(NetworkRole role, NetworkFlowTemplateNode node) {
		return new NetworkFlowBuilder(new OnRoleNode(role, node));
	}

	public NetworkFlowBuilder then(NetworkFlowTemplateNode node) {
		nodes.add(node);
		return this;
	}

	public NetworkFlowBuilder thenSend(SyncedEvent event) {
		nodes.add(new SendNode(event));
		return this;
	}

	public NetworkFlowBuilder thenSend(Function<SyncedEvent, SyncedEvent> eventProvider) {
		nodes.add(new SendNode(eventProvider));
		return this;
	}

	public NetworkFlowBuilder thenExpect(Class<? extends SyncedEvent> eventClass) {
		nodes.add(new ExpectNode(eventClass));
		return this;
	}

	public NetworkFlowTemplateNode build() {
		if (nodes.size() == 1) {
			return nodes.get(0);
		}
		return new SequenceNode(nodes);
	}

}
