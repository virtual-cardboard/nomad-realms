package nomadrealms.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import nomadrealms.networking.NetworkGraph;
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
		List<Player> onlinePlayers = new CopyOnWriteArrayList<>();
		ServerSyncedEventHandler serverHandler = new ServerSyncedEventHandler(serverNode, onlinePlayers);

		PacketAddress serverAddress = new PacketAddress(InetAddress.getByName("127.0.0.1"), serverPort);

		// Client 1 setup
		NetworkGraph client1State = new NetworkGraph();
		client1State.init();
		List<Player> client1ReceivedPlayers = new ArrayList<>();
		ClientSyncedEventHandler client1Handler = new ClientSyncedEventHandler(client1ReceivedPlayers, client1State);

		// Client 2 setup
		NetworkNode client2Node = new NetworkNode();
		client2Node.init(0);

		try {
			// Connect Client 1
			client1State.send(new ConnectToServerEvent("Player 1"), serverAddress);
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
			client1State.update(client1Handler::handle); // Client 1 processes response

			// Verify
			assertEquals(1, client1ReceivedPlayers.size(), "Client 1 should see one other player (Player 2) automatically");
			assertEquals("Player 2", client1ReceivedPlayers.get(0).name());

		} finally {
			serverNode.cleanUp();
			client1State.cleanUp();
			client2Node.cleanUp();
		}
	}

}
