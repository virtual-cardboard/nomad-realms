package context;

import static address.ServerAddress.SERVER_ADDRESS;
import static protocol.BootstrapProtocol.BOOTSTRAP_RESPONSE;
import static protocol.BootstrapProtocol.PEER_CONNECT_REQUEST;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import common.GameInputEventHandler;
import common.source.NetworkSource;
import context.input.GameInput;
import context.input.networking.packet.address.PacketAddress;
import context.input.networking.packet.address.PeerAddress;
import context.input.networking.packet.block.PacketBlock;
import context.input.networking.packet.block.PacketBlockReader;
import event.BootstrapResponseEvent;
import event.PeerConnectEvent;

public class BootstrapInput extends GameInput {

	public BootstrapInput() {
		addPacketReceivedFunction(new GameInputEventHandler<>((event) -> {
			NetworkSource source = (NetworkSource) event.getSource();
			if (source.getAddress().equals(SERVER_ADDRESS)) {
				List<PacketBlock> blocks = event.getModel().blocks();
				if (blocks.size() != 1) {
					throw new RuntimeException("Expected block of length 1");
				}
				PacketBlockReader reader = BOOTSTRAP_RESPONSE.reader(blocks.get(0));
				long timestamp = reader.readLong();
				long nonce = reader.readLong();
				System.out.println("Nonce: " + nonce);
				InetAddress lanIP;
				try {
					lanIP = InetAddress.getByAddress(reader.readIPv4());
				} catch (UnknownHostException e) {
					e.printStackTrace();
					return null;
				}
				System.out.println("Peer LAN ip: " + lanIP);
				short lanPort = reader.readShort();
				System.out.println("Peer LAN port: " + lanPort);
				InetAddress wanIP;
				try {
					wanIP = InetAddress.getByAddress(reader.readIPv4());
				} catch (UnknownHostException e) {
					e.printStackTrace();
					return null;
				}
				System.out.println("Peer WAN ip: " + lanIP);
				short wanPort = reader.readShort();
				System.out.println("Peer WAN port: " + wanPort);
				PacketAddress lanAddress = new PeerAddress(lanIP, lanPort);
				PacketAddress wanAddress = new PeerAddress(wanIP, wanPort);
				System.out.println("Reported by: " + source.getAddress());
				return new BootstrapResponseEvent(source, timestamp, nonce, lanAddress, wanAddress);
			} else {
				List<PacketBlock> blocks = event.getModel().blocks();
				if (blocks.size() != 1) {
					throw new RuntimeException("Expected block of length 1");
				}
				PacketBlockReader reader = PEER_CONNECT_REQUEST.reader(blocks.get(0));
				long timestamp = reader.readLong();
				long nonce = reader.readLong();
				return new PeerConnectEvent(source, timestamp, nonce);
			}
		}));
	}

}
