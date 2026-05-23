package nomadrealms.networking;

import engine.networking.graph.NetworkGraph;
import engine.networking.graph.NetworkRole;
import engine.networking.messenger.NetworkMessenger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nomadrealms.user.Player;

public class NomadsNetworkGraph extends NetworkGraph {

	private final NetworkMessenger networkNode = new NetworkMessenger();
	private final List<NomadsNetworkConnection> connections = new ArrayList<>();

	public NomadsNetworkGraph(NetworkRole role) {
		super(role);
	}

	public List<NomadsNetworkConnection> connections() {
		return connections;
	}

	public void addConnection(NomadsNetworkConnection connection) {
		connections.add(connection);
	}

	public Optional<NomadsNetworkConnection> getConnection(UUID nonce) {
		return connections.stream()
				.filter(c -> c.nonce().equals(nonce))
				.findFirst();
	}

	public Optional<NomadsNetworkConnection> getConnection(Player player) {
		return connections.stream()
				.filter(c -> c.player().name().equals(player.name()))
				.findFirst();
	}

}
