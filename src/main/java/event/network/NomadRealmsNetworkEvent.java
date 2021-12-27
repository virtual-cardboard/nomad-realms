package event.network;

import static networking.NetworkUtils.PROTOCOL_ID;

import common.source.NetworkSource;
import context.input.networking.packet.PacketBuilder;
import context.input.networking.packet.PacketModel;
import context.input.networking.packet.address.PacketAddress;
import event.game.NomadRealmsGameEvent;
import networking.protocols.NomadRealmsNetworkProtocols;

public abstract class NomadRealmsNetworkEvent extends NomadRealmsGameEvent {

	public NomadRealmsNetworkEvent(NetworkSource source) {
		super(source);
	}

	public NomadRealmsNetworkEvent(long time, NetworkSource source) {
		super(time, source);
	}

	@Override
	public NetworkSource source() {
		return (NetworkSource) super.source();
	}

	public PacketModel toPacket(PacketAddress address) {
		PacketBuilder builder = PROTOCOL_ID.builder(address).consume(protocolID().id());
		return toPacketModel(builder);
	}

	protected abstract PacketModel toPacketModel(PacketBuilder builder);

	protected abstract NomadRealmsNetworkProtocols protocolID();

}
