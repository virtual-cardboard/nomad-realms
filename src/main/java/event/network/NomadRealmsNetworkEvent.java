package event.network;

import static networking.NetworkUtils.PROTOCOL_ID;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.address.PacketAddress;
import event.game.NomadRealmsGameEvent;
import networking.protocols.NomadRealmsNetworkProtocol;

public abstract class NomadRealmsNetworkEvent extends NomadRealmsGameEvent {

	private NetworkSource source;

	public NomadRealmsNetworkEvent(NetworkSource source) {
		this.source = source;
	}

	public NomadRealmsNetworkEvent(long time, NetworkSource source) {
		super(time);
		this.source = source;
	}

	public NetworkSource source() {
		return source;
	}

	public PacketModel toPacket(PacketAddress address) {
		PacketBuilder idBuilder = PROTOCOL_ID.builder(address).consume(protocol().id());
		PacketBuilder builder = protocol().format().builder(idBuilder);
		return toPacketModel(builder);
	}

	protected abstract PacketModel toPacketModel(PacketBuilder builder);

	protected abstract NomadRealmsNetworkProtocol protocol();

}
