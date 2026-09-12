package nomadrealms.networking;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import engine.context.input.networking.packet.address.PacketAddress;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;
import nomadrealms.event.networking.HeartbeatSyncedEvent;
import nomadrealms.event.networking.SyncedEvent;
import nomadrealms.event.networking.SyncedEventDerializer;
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.user.Player;
import org.junit.jupiter.api.Test;

public class HeartbeatTest {

	@Test
	public void testHeartbeatSerialization() {
		UUID nonce = UUID.randomUUID();
		HeartbeatSyncedEvent event = new HeartbeatSyncedEvent(nonce);
		byte[] serialized = SyncedEventDerializer.serialize(event);
		SyncedEvent deserialized = SyncedEventDerializer.deserialize(serialized);

		assertNotNull(deserialized);
		assertTrue(deserialized instanceof HeartbeatSyncedEvent);
		assertEquals(nonce, ((HeartbeatSyncedEvent) deserialized).nonce());
	}

	@Test
	public void testConnectionStateTransitions() throws Exception {
		PacketAddress address = new PacketAddress(InetAddress.getByName("127.0.0.1"), 12345);
		Player player = new Player("TestPlayer", address);

		UUID nonce = UUID.randomUUID();
		Connection connection = new Connection(player, nonce);

		// Initially LISTENING
		assertEquals(ConnectionState.LISTENING, connection.state());

		// Transition to HEALTHY
		connection.state(ConnectionState.HEALTHY);
		assertEquals(ConnectionState.HEALTHY, connection.state());

		// Simulate passage of 3.5s without heartbeat -> STALE
		connection.lastReceivedHeartbeat(System.currentTimeMillis() - 3500);

		NetworkGraph graph = new NetworkGraph();
		graph.addConnection(connection);
		ClientSyncedEventHandler handler = new ClientSyncedEventHandler(new ArrayList<>(), graph);

		graph.update(handler::handle);
		assertEquals(ConnectionState.STALE, connection.state());

		// Receiving a heartbeat should transition STALE back to HEALTHY
		handler.handle(new HeartbeatSyncedEvent(nonce), address);
		assertEquals(ConnectionState.HEALTHY, connection.state());

		// Simulate passage of 10.5s without heartbeat -> TERMINATED
		connection.lastReceivedHeartbeat(System.currentTimeMillis() - 10500);
		graph.update(handler::handle);
		assertEquals(ConnectionState.TERMINATED, connection.state());

		// Receiving a heartbeat when TERMINATED should NOT recover to HEALTHY
		handler.handle(new HeartbeatSyncedEvent(nonce), address);
		assertEquals(ConnectionState.TERMINATED, connection.state());
	}

}
