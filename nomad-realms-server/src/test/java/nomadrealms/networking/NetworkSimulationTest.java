package nomadrealms.networking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import engine.context.input.networking.packet.address.PacketAddress;
import engine.networking.NetworkNode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import nomadrealms.event.networking.JoinGameSyncedEvent;
import nomadrealms.event.networking.handler.ClientSyncedEventHandler;
import nomadrealms.event.networking.handler.ServerSyncedEventHandler;
import org.junit.jupiter.api.Test;

public class NetworkSimulationTest {

	@Test
	public void testNetworkSimulation() throws UnknownHostException, InterruptedException {
		NetworkNode serverNode = new NetworkNode();
		serverNode.init(0);
		ServerSyncedEventHandler serverHandler = new ServerSyncedEventHandler(serverNode);

		NetworkNode client1Node = new NetworkNode();
		client1Node.init(0);
		ClientSyncedEventHandler client1Handler = new ClientSyncedEventHandler();

		NetworkNode client2Node = new NetworkNode();
		client2Node.init(0);
		ClientSyncedEventHandler client2Handler = new ClientSyncedEventHandler();

		PacketAddress serverAddress = new PacketAddress(InetAddress.getLocalHost(), serverNode.port());

		client1Node.send(new JoinGameSyncedEvent("Client 1"), serverAddress);
		client2Node.send(new JoinGameSyncedEvent("Client 2"), serverAddress);

		long startTime = System.currentTimeMillis();
		while (System.currentTimeMillis() - startTime < 2000) {
			serverNode.update(serverHandler::handle);
			client1Node.update(client1Handler::handle);
			client2Node.update(client2Handler::handle);
			if (client1Handler.players().size() == 2 && client2Handler.players().size() == 2) {
				break;
			}
			Thread.sleep(10);
		}

		assertEquals(2, client1Handler.players().size());
		assertEquals(2, client2Handler.players().size());

		assertEquals("Client 1", client1Handler.players().get(0).name());
		assertEquals("Client 2", client1Handler.players().get(1).name());

		assertEquals("Client 1", client2Handler.players().get(0).name());
		assertEquals("Client 2", client2Handler.players().get(1).name());

		serverNode.cleanUp();
		client1Node.cleanUp();
		client2Node.cleanUp();
	}

}
