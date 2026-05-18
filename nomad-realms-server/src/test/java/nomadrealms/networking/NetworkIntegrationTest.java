package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.networking.NetworkGraph;
import nomadrealms.networking.flow.NetworkRole;
import nomadrealms.user.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkIntegrationTest {

	@Test
	public void testNetworkJoinAndPlayerList() throws Exception {
		// Server setup
		NetworkNode serverNode = new NetworkNode();
		serverNode.init(0); // Random port
		int serverPort = serverNode.port();
		List<Player> serverOnlinePlayers = new CopyOnWriteArrayList<>();
		NetworkGraph serverGraph = new NetworkGraph(NetworkRole.SERVER);

		PacketAddress serverAddress = new PacketAddress(InetAddress.getByName("127.0.0.1"), serverPort);

		// Client 1 setup
		NetworkNode client1Node = new NetworkNode();
		client1Node.init(0);
		NetworkGraph client1Graph = new NetworkGraph(NetworkRole.CLIENT);
		List<Player> client1ReceivedPlayers = new ArrayList<>();

		// Client 2 setup
		NetworkNode client2Node = new NetworkNode();
		client2Node.init(0);

		try {
			// Connect Client 1
			client1Node.send(new ConnectToServerEvent("Player 1"), serverAddress);
			Thread.sleep(200);
			serverGraph.update(serverNode, serverOnlinePlayers);
			// serverOnlinePlayers should have 1 player
			assertEquals(1, serverOnlinePlayers.size());

			// Connect Client 2
			client2Node.send(new ConnectToServerEvent("Player 2"), serverAddress);
			Thread.sleep(200);
			serverGraph.update(serverNode, serverOnlinePlayers);
			// serverOnlinePlayers should have 2 players
			assertEquals(2, serverOnlinePlayers.size());

			// Verify Client 1 automatically received update
			Thread.sleep(200);
			client1Graph.update(client1Node, client1ReceivedPlayers); // Client 1 processes response

			// Verify
			System.out.println("Client 1 players: " + client1ReceivedPlayers.size());
			for (Player p : client1ReceivedPlayers) {
				System.out.println(" - " + p.name() + " (" + p.address() + ")");
			}
			assertEquals(1, client1ReceivedPlayers.size(), "Client 1 should see one other player (Player 2) automatically");
			assertEquals("Player 2", client1ReceivedPlayers.get(0).name());

		} finally {
			serverNode.cleanUp();
			client1Node.cleanUp();
			client2Node.cleanUp();
		}
	}

}
