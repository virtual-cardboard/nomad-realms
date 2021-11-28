package context.bootstrap.input;

import static protocol.BootstrapProtocol.BOOTSTRAP_RESPONSE;
import static protocol.BootstrapProtocol.PEER_CONNECT_REQUEST;
import static protocol.NetworkUtils.PROTOCOL_FORMAT;
import static protocol.NetworkUtils.addressFromBytes;
import static protocol.address.ServerAddress.SERVER_ADDRESS;

import java.net.InetAddress;
import java.util.function.Function;

import common.event.GameEvent;
import common.source.NetworkSource;
import context.input.event.PacketReceivedInputEvent;
import context.input.networking.packet.PacketReader;
import context.input.networking.packet.address.PacketAddress;
import event.connect.BootstrapResponseEvent;
import event.connect.PeerConnectEvent;

public class BootstrapResponseReader implements Function<PacketReceivedInputEvent, GameEvent> {

	@Override
	public GameEvent apply(PacketReceivedInputEvent event) {
		System.out.println("Received packet from: " + event.source().getAddress());
		if (event.source().getAddress().equals(SERVER_ADDRESS)) {
			return handleServerPacket(event);
		} else {
			PacketReader reader = PEER_CONNECT_REQUEST.reader(event.model());
			long timestamp = reader.readLong();
			long nonce = reader.readLong();
			return new PeerConnectEvent(event.source(), timestamp, nonce);
		}
	}

	private GameEvent handleServerPacket(PacketReceivedInputEvent event) {
		PacketReader protocolReader = PROTOCOL_FORMAT.reader(event.model());
		short packetID = protocolReader.readShort();
		switch (packetID) {
			case 100:
				return handleBootstrapResponse(event.source(), protocolReader);
			default:
				System.out.println("Unknown protocol");
				return null;
		}
	}

	private GameEvent handleBootstrapResponse(NetworkSource source, PacketReader protocolReader) {
		PacketReader reader = BOOTSTRAP_RESPONSE.reader(protocolReader);

		long timestamp = reader.readLong();
		long nonce = reader.readLong();
		System.out.println("Nonce: " + nonce);

		InetAddress lanIP = addressFromBytes(reader.readIPv4());
		short lanPort = reader.readShort();
		InetAddress wanIP = addressFromBytes(reader.readIPv4());
		short wanPort = reader.readShort();
		System.out.println("Peer LAN ip: " + lanIP);
		System.out.println("Peer LAN port: " + lanPort);
		System.out.println("Peer WAN ip: " + wanIP);
		System.out.println("Peer WAN port: " + wanPort);
		PacketAddress lanAddress = new PacketAddress(lanIP, lanPort);
		PacketAddress wanAddress = new PacketAddress(wanIP, wanPort);

		String username = reader.readString();
		System.out.println("Peer Username: " + username);

		return new BootstrapResponseEvent(source, timestamp, nonce, lanAddress, wanAddress, username);
	}

}
