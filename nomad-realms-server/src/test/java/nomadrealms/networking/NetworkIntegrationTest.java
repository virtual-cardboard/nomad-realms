package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.networking.flow.FlowContext;
import nomadrealms.networking.flow.NetworkRole;
import nomadrealms.user.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkIntegrationTest {

	@Test
	public void testNetworkJoinAndPlayerList() throws Exception {
		// Server setup
		NetworkGraph serverGraph = new NetworkGraph();
		serverGraph.init(0); // Random port
		int serverPort = serverGraph.port();
		List<Player> onlinePlayers = new CopyOnWriteArrayList<>();
		FlowContext serverCtx = new FlowContext(serverGraph, onlinePlayers, null);

		PacketAddress serverAddress = new PacketAddress(InetAddress.getByName("127.0.0.1"), serverPort);

		// Client 1 setup
		NetworkGraph client1Graph = new NetworkGraph();
		client1Graph.init();
		List<Player> client1ReceivedPlayers = new ArrayList<>();
		Player client1LocalPlayer = new Player("Player 1", null);
		FlowContext client1Ctx = new FlowContext(client1Graph, client1ReceivedPlayers, client1LocalPlayer);

		// Client 2 setup
		NetworkGraph client2Graph = new NetworkGraph();
		client2Graph.init();
		List<Player> client2ReceivedPlayers = new ArrayList<>();
		Player client2LocalPlayer = new Player("Player 2", null);
		FlowContext client2Ctx = new FlowContext(client2Graph, client2ReceivedPlayers, client2LocalPlayer);

		try {
			// Connect Client 1
			client1Graph.send(new ConnectToServerEvent("Player 1"), serverAddress);
			Thread.sleep(200);
			serverGraph.update(serverCtx, NetworkRole.SERVER);
			assertEquals(1, onlinePlayers.size());

			// Connect Client 2
			client2Graph.send(new ConnectToServerEvent("Player 2"), serverAddress);
			Thread.sleep(200);
			serverGraph.update(serverCtx, NetworkRole.SERVER);
			assertEquals(2, onlinePlayers.size());

			// Verify Client 1 automatically received update
			Thread.sleep(200);
			client1Graph.update(client1Ctx, NetworkRole.CLIENT);

			// Verify
			assertEquals(1, client1ReceivedPlayers.size(), "Client 1 should see one other player (Player 2) automatically");
			assertEquals("Player 2", client1ReceivedPlayers.get(0).name());

		} finally {
			serverGraph.cleanUp();
			client1Graph.cleanUp();
			client2Graph.cleanUp();
		}
	}

}
