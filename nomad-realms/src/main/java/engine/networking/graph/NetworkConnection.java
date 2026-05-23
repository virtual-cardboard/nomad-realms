package engine.networking.graph;

public class NetworkConnection {

	protected ConnectionState state;
	protected final NetworkNode node;

	public NetworkConnection(ConnectionState connectionState, NetworkNode node) {
		this.state = connectionState;
		this.node = node;
	}

	public ConnectionState state() {
		return state;
	}

	public void state(ConnectionState state) {
		this.state = state;
	}

	public NetworkNode node() {
		return node;
	}

}
