package context.misc;

import static protocol.BootstrapProtocol.BOOTSTRAP_RESPONSE;
import static protocol.BootstrapProtocol.PEER_CONNECT_REQUEST;
import static protocol.address.ServerAddress.SERVER_ADDRESS;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.GameInputEventHandler;
import common.source.NetworkSource;
import context.input.GameInput;
import context.input.networking.packet.PacketReader;
import context.input.networking.packet.address.PacketAddress;
import event.BootstrapResponseEvent;
import event.PeerConnectEvent;

public class BootstrapInput extends GameInput {

	public BootstrapInput() {
		addPacketReceivedFunction(new GameInputEventHandler<>((event) -> {
			NetworkSource source = (NetworkSource) event.source();
			if (source.getAddress().equals(SERVER_ADDRESS)) {
				PacketReader reader = BOOTSTRAP_RESPONSE.reader(event.getModel());
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
				System.out.println("Peer WAN ip: " + wanIP);
				short wanPort = reader.readShort();
				System.out.println("Peer WAN port: " + wanPort);
				PacketAddress lanAddress = new PacketAddress(lanIP, lanPort);
				PacketAddress wanAddress = new PacketAddress(wanIP, wanPort);
				System.out.println("Reported by: " + source.getAddress());
				return new BootstrapResponseEvent(source, timestamp, nonce, lanAddress, wanAddress);
			} else {
				PacketReader reader = PEER_CONNECT_REQUEST.reader(event.getModel());
				long timestamp = reader.readLong();
				long nonce = reader.readLong();
				return new PeerConnectEvent(source, timestamp, nonce);
			}
		}));
	}

}
