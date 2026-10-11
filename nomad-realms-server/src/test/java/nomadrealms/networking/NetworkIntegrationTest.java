package nomadrealms.networking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import nomadrealms.event.networking.HolePunchEvent;
import nomadrealms.event.networking.HolePunchInitiationEvent;
import nomadrealms.event.networking.HolePunchSuccessConfirmationEvent;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import nomadrealms.render.ui.custom.console.Console;
import nomadrealms.user.Player;
import org.junit.jupiter.api.Test;

public class NetworkIntegrationTest {

	@Test
	public void testNetworkJoinAndPlayerList() throws Exception {
		// Server setup
		NetworkNode serverNode = new NetworkNode();
		serverNode.init(0); // Random port
		int serverPort = serverNode.port();
		List<Player> onlinePlayers = new CopyOnWriteArrayList<>();
		Console console = new Console(null, null, null);
		ServerSyncedEventHandler serverHandler = new ServerSyncedEventHandler(serverNode, onlinePlayers, console);

		PacketAddress serverAddress = new PacketAddress(InetAddress.getByName("127.0.0.1"), serverPort);

		// Client 1 setup
		NetworkGraph client1Graph = new NetworkGraph();
		client1Graph.init();
		List<Player> client1ReceivedPlayers = new ArrayList<>();
		ClientSyncedEventHandler client1Handler = new ClientSyncedEventHandler(client1ReceivedPlayers, client1Graph);

		// Client 2 setup
		NetworkNode client2Node = new NetworkNode();
		client2Node.init(0);

		try {
			// Connect Client 1
			client1Graph.send(new ConnectToServerEvent("Player 1"), serverAddress);
			Thread.sleep(200);
			serverNode.update(serverHandler::handle);
			assertEquals(1, onlinePlayers.size());

			// Connect Client 2
			client2Node.send(new ConnectToServerEvent("Player 2"), serverAddress);
			Thread.sleep(200);
			serverNode.update(serverHandler::handle);
			assertEquals(2, onlinePlayers.size());

			// Verify Client 1 automatically received update
			Thread.sleep(200);
			client1Graph.update(client1Handler::handle); // Client 1 processes response

			// Verify
			assertEquals(1, client1ReceivedPlayers.size(), "Client 1 should see one other player (Player 2) automatically");
			assertEquals("Player 2", client1ReceivedPlayers.get(0).name());

		} finally {
			serverNode.cleanUp();
			client1Graph.cleanUp();
			client2Node.cleanUp();
		}
	}

	@Test
	public void testServerAndThreePeersHolePunchConnection() throws Exception {
		// Server setup
		NetworkNode serverNode = new NetworkNode();
		serverNode.init(0);
		int serverPort = serverNode.port();
		List<Player> onlinePlayers = new CopyOnWriteArrayList<>();
		Console console = new Console(null, null, null);
		ServerSyncedEventHandler serverHandler = new ServerSyncedEventHandler(serverNode, onlinePlayers, console);

		PacketAddress serverAddress = new PacketAddress(InetAddress.getByName("127.0.0.1"), serverPort);

		// Peer 1 setup
		NetworkGraph peer1Graph = new NetworkGraph();
		peer1Graph.init();
		List<Player> peer1OnlinePlayers = new ArrayList<>();
		ClientSyncedEventHandler peer1Handler = new ClientSyncedEventHandler(peer1OnlinePlayers, peer1Graph);

		// Peer 2 setup
		NetworkGraph peer2Graph = new NetworkGraph();
		peer2Graph.init();
		List<Player> peer2OnlinePlayers = new ArrayList<>();
		ClientSyncedEventHandler peer2Handler = new ClientSyncedEventHandler(peer2OnlinePlayers, peer2Graph);

		// Peer 3 setup
		NetworkGraph peer3Graph = new NetworkGraph();
		peer3Graph.init();
		List<Player> peer3OnlinePlayers = new ArrayList<>();
		ClientSyncedEventHandler peer3Handler = new ClientSyncedEventHandler(peer3OnlinePlayers, peer3Graph);

		List<NetworkGraph> clientGraphs = new ArrayList<>();
		clientGraphs.add(peer1Graph);
		clientGraphs.add(peer2Graph);
		clientGraphs.add(peer3Graph);

		List<ClientSyncedEventHandler> clientHandlers = new ArrayList<>();
		clientHandlers.add(peer1Handler);
		clientHandlers.add(peer2Handler);
		clientHandlers.add(peer3Handler);

		try {
			// Step 1: Peer 1 and Peer 2 join server
			peer1Graph.send(new ConnectToServerEvent("Peer 1"), serverAddress);
			peer2Graph.send(new ConnectToServerEvent("Peer 2"), serverAddress);

			// Step 2: Tick network so server receives connections and peers receive updated player lists
			tickNetworkCycles(serverNode, serverHandler, clientGraphs, clientHandlers, 5);

			assertEquals(2, onlinePlayers.size());

			// Step 3: Peer 1 initiates hole punching to connect with Peer 2
			peer1Graph.send(new HolePunchInitiationEvent(), serverAddress);

			// Step 4: Tick network until Peer 1 and Peer 2 establish HEALTHY connection
			tickNetworkCycles(serverNode, serverHandler, clientGraphs, clientHandlers, 10);

			assertEquals(1, peer1Graph.connections().size());
			assertEquals(ConnectionState.HEALTHY, peer1Graph.connections().get(0).state());

			assertEquals(1, peer2Graph.connections().size());
			assertEquals(ConnectionState.HEALTHY, peer2Graph.connections().get(0).state());

			// Step 5: Peer 3 joins server
			peer3Graph.send(new ConnectToServerEvent("Peer 3"), serverAddress);
			tickNetworkCycles(serverNode, serverHandler, clientGraphs, clientHandlers, 5);

			assertEquals(3, onlinePlayers.size());

			// Step 6: Peer 3 initiates hole punching to connect with existing peers (Peer 1 and Peer 2)
			peer3Graph.send(new HolePunchInitiationEvent(), serverAddress);
			tickNetworkCycles(serverNode, serverHandler, clientGraphs, clientHandlers, 10);

			// Step 7: Verify all 3 peers are connected to each other (2 connections each) and all in HEALTHY state
			assertEquals(2, peer1Graph.connections().size());
			for (Connection conn : peer1Graph.connections()) {
				assertEquals(ConnectionState.HEALTHY, conn.state(), "Peer 1 connection to " + conn.player().name() + " should be HEALTHY");
			}

			assertEquals(2, peer2Graph.connections().size());
			for (Connection conn : peer2Graph.connections()) {
				assertEquals(ConnectionState.HEALTHY, conn.state(), "Peer 2 connection to " + conn.player().name() + " should be HEALTHY");
			}

			assertEquals(2, peer3Graph.connections().size());
			for (Connection conn : peer3Graph.connections()) {
				assertEquals(ConnectionState.HEALTHY, conn.state(), "Peer 3 connection to " + conn.player().name() + " should be HEALTHY");
			}

		} finally {
			serverNode.cleanUp();
			peer1Graph.cleanUp();
			peer2Graph.cleanUp();
			peer3Graph.cleanUp();
		}
	}

	private void tickNetworkCycles(NetworkNode serverNode, ServerSyncedEventHandler serverHandler,
								  List<NetworkGraph> clientGraphs, List<ClientSyncedEventHandler> clientHandlers,
								  int cycles) throws InterruptedException {
		for (int c = 0; c < cycles; c++) {
			Thread.sleep(50);
			serverNode.update(serverHandler::handle);
			for (int i = 0; i < clientGraphs.size(); i++) {
				NetworkGraph graph = clientGraphs.get(i);
				ClientSyncedEventHandler handler = clientHandlers.get(i);
				graph.update(handler::handle);
			}
		}
	}

}
