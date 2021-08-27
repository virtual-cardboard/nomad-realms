package context;

import static address.ServerAddress.SERVER_ADDRESS;
import static java.lang.System.currentTimeMillis;
import static protocol.BootstrapProtocol.BOOTSTRAP_REQUEST;
import static protocol.BootstrapProtocol.PEER_CONNECT_REQUEST;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import common.event.GameEvent;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.block.PacketBlock;
import context.logic.GameLogic;
import event.BootstrapResponseEvent;
import event.PeerConnectEvent;

public class BootstrapLogic extends GameLogic {

	private long nonce;

	@Override
	public void update() {
		BootstrapData data = (BootstrapData) getContext().getData();
		if (!data.sentBootstrap) {
			System.out.println("Sending bootstrap packet to server at " + SERVER_ADDRESS);
			InetAddress localAddress = null;
			try (Socket socket = new Socket()) {
				socket.connect(new InetSocketAddress("google.com", 80));
				localAddress = socket.getLocalAddress();
			} catch (IOException e) {
				e.printStackTrace();
			}
			PacketBlock bootstrapBody = BOOTSTRAP_REQUEST.builder()
					.consume(currentTimeMillis())
					.consume(localAddress)
					.consume(getContext().getSocketPort())
					.build();
			PacketModel packet = new PacketModel(SERVER_ADDRESS, bootstrapBody);
			getContext().sendPacket(packet);
			data.sentBootstrap = true;
			System.out.println("Done sending bootstrap packet");
		}
		while (!getEventQueue().isEmpty()) {
			GameEvent event = getEventQueue().poll();
			if (!data.receivedBootstrap) {
				if (event instanceof BootstrapResponseEvent) {
					System.out.println("Received bootstrap response");
					BootstrapResponseEvent bootstrapResponseEvent = (BootstrapResponseEvent) event;
					nonce = bootstrapResponseEvent.getNonce();
					System.out.println("Nonce: " + nonce);
					PacketBlock bootstrapBody = PEER_CONNECT_REQUEST.builder()
							.consume(currentTimeMillis())
							.consume(nonce)
							.build();
					PacketModel wanPacket = new PacketModel(bootstrapResponseEvent.getWanAddress(), bootstrapBody);
					PacketModel lanPacket = new PacketModel(bootstrapResponseEvent.getLanAddress(), bootstrapBody);
					System.out.println("Sending to " + bootstrapResponseEvent.getWanAddress() + " and " + bootstrapResponseEvent.getLanAddress());
					getContext().sendPacket(wanPacket);
					getContext().sendPacket(lanPacket);
					System.out.println("Done sending peer connect packet");
					data.receivedBootstrap = true;
				} else {
					throw new RuntimeException("Was expecting Bootstrap Response");
				}
			} else {
				if (event instanceof PeerConnectEvent) {
					PeerConnectEvent peerConnectEvent = (PeerConnectEvent) event;
					System.out.println("Received peer connect response");
					System.out.println("Received nonce: " + peerConnectEvent.getNonce());
					System.out.println("Peer address: " + peerConnectEvent.getSource().getDescription());
				}
			}
		}
	}

}
