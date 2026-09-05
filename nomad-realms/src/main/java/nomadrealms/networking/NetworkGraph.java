package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import nomadrealms.event.networking.HeartbeatSyncedEvent;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.user.Player;

public class NetworkGraph {

	private final NetworkNode networkNode = new NetworkNode();
	private final List<Connection> connections = new ArrayList<>();

	public void init() {
		networkNode.init();
	}

	public void update(BiConsumer<SyncedEvent, PacketAddress> handler) {
		networkNode.update(handler);
		long currentTime = System.currentTimeMillis();
		for (Connection connection : connections) {
			if (connection.state() == ConnectionState.HEALTHY || connection.state() == ConnectionState.STALE) {
				if (currentTime - connection.lastSentHeartbeat() >= 2000) {
					send(new HeartbeatSyncedEvent(connection.nonce()), connection.targetAddress());
					connection.lastSentHeartbeat(currentTime);
				}

				long timeSinceLastReceived = currentTime - connection.lastReceivedHeartbeat();
				if (timeSinceLastReceived >= 10000) {
					connection.state(ConnectionState.TERMINATED);
				} else if (timeSinceLastReceived >= 3000) {
					connection.state(ConnectionState.STALE);
				}
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

}
