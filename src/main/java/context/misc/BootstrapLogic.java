package context.misc;

import static java.lang.System.currentTimeMillis;
import static protocol.BootstrapProtocol.BOOTSTRAP_REQUEST;
import static protocol.BootstrapProtocol.PEER_CONNECT_REQUEST;
import static protocol.address.ServerAddress.SERVER_ADDRESS;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import context.input.networking.packet.PacketModel;
import context.logic.GameLogic;
import event.connect.BootstrapResponseEvent;
import event.connect.PeerConnectEvent;

public class BootstrapLogic extends GameLogic {

	private long nonce;

	@Override
	protected void init() {
		addHandler(BootstrapResponseEvent.class, event -> {
			BootstrapData data = (BootstrapData) context().data();
			if (!data.receivedBootstrap) {
				System.out.println("Received bootstrap response");
				BootstrapResponseEvent bootstrapResponseEvent = event;
				nonce = bootstrapResponseEvent.getNonce();
				System.out.println("Nonce: " + nonce);
				PacketModel wanPacket = PEER_CONNECT_REQUEST.builder(bootstrapResponseEvent.getWanAddress())
						.consume(currentTimeMillis())
						.consume(nonce)
						.build();
				PacketModel lanPacket = PEER_CONNECT_REQUEST.builder(bootstrapResponseEvent.getLanAddress())
						.consume(currentTimeMillis())
						.consume(nonce)
						.build();
				System.out.println("Sending to " + bootstrapResponseEvent.getWanAddress() + " and " + bootstrapResponseEvent.getLanAddress());
				context().sendPacket(wanPacket);
				context().sendPacket(lanPacket);
				System.out.println("Done sending peer connect packet");
				data.receivedBootstrap = true;
			} else {
				throw new RuntimeException("Was expecting Bootstrap Response");
			}
		});
		addHandler(PeerConnectEvent.class, event -> {
			System.out.println("Received peer connect response");
			System.out.println("Received nonce: " + event.getNonce());
			System.out.println("Peer address: " + event.source().description());
		});
	}

	@Override
	public void update() {
		BootstrapData data = (BootstrapData) context().data();
		if (!data.sentBootstrap) {
			System.out.println("Sending bootstrap packet to server at " + SERVER_ADDRESS);
			InetAddress localAddress = null;
			try (Socket socket = new Socket()) {
				socket.connect(new InetSocketAddress("google.com", 80));
				localAddress = socket.getLocalAddress();
			} catch (IOException e) {
				e.printStackTrace();
			}
			PacketModel packet = BOOTSTRAP_REQUEST.builder(SERVER_ADDRESS)
					.consume(currentTimeMillis())
					.consume(localAddress)
					.consume(context().socketPort())
					.build();
			context().sendPacket(packet);
			data.sentBootstrap = true;
			System.out.println("Done sending bootstrap packet");
		}
	}

}
