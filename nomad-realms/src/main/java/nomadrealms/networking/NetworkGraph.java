package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
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
