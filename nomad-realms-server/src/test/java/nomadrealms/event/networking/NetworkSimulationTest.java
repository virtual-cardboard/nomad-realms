package nomadrealms.event.networking;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import nomadrealms.event.networking.bootstrap.ConnectToServerEvent;
import nomadrealms.event.networking.bootstrap.GetOnlinePlayersEvent;
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import nomadrealms.user.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NetworkSimulationTest {

	private NetworkNode serverNode;
	private NetworkNode client1Node;
	private NetworkNode client2Node;

	private List<Player> serverOnlinePlayers;
	private ServerSyncedEventHandler serverHandler;

	private List<Player> client1ReceivedPlayers;
	private ClientSyncedEventHandler client1Handler;

	private PacketAddress serverAddress;

	@BeforeEach
	public void setup() throws Exception {
		serverOnlinePlayers = new CopyOnWriteArrayList<>();
		serverNode = new NetworkNode();
		serverNode.init(44999);
		serverHandler = new ServerSyncedEventHandler(serverNode, serverOnlinePlayers);

		client1Node = new NetworkNode();
		client1Node.init(0);
		client1ReceivedPlayers = new ArrayList<>();
		client1Handler = new ClientSyncedEventHandler(players -> {
			System.out.println("Callback received players: " + players.size());
			client1ReceivedPlayers.clear();
			client1ReceivedPlayers.addAll(players);
		});

		client2Node = new NetworkNode();
		client2Node.init(0);

		serverAddress = new PacketAddress(InetAddress.getByName("127.0.0.1"), 44999);
	}

	@AfterEach
	public void tearDown() {
		serverNode.cleanUp();
		client1Node.cleanUp();
		client2Node.cleanUp();
	}

	@Test
	public void testJoinAndPlayerList() throws InterruptedException {
		// Step 1: Players join
		client1Node.send(new ConnectToServerEvent("Player 1"), serverAddress);
		client2Node.send(new ConnectToServerEvent("Player 2"), serverAddress);

		// Give some time for packets to travel and be processed
		for (int i = 0; i < 20; i++) {
			serverNode.update(serverHandler::handle);
			Thread.sleep(50);
		}

		assertEquals(2, serverOnlinePlayers.size(), "Server should have 2 online players");

		// Step 2: Client 1 requests online players
		client1Node.send(new GetOnlinePlayersEvent(), serverAddress);

		// Give some time for request to reach server and response to reach client
		for (int i = 0; i < 50; i++) {
			serverNode.update(serverHandler::handle);
			client1Node.update((event, address) -> {
				System.out.println("Client 1 received event type: " + event.getClass().getSimpleName());
				client1Handler.handle(event, address);
			});
			Thread.sleep(50);
		}

		assertEquals(1, client1ReceivedPlayers.size(), "Client 1 should have received 1 other player");
		assertEquals("Player 2", client1ReceivedPlayers.get(0).name());
	}
}
