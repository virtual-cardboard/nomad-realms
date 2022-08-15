package event.network;

import derealizer.format.Derealizable;
import engine.common.networking.packet.PacketModel;
import engine.common.networking.packet.address.PacketAddress;
import engine.common.source.NetworkSource;
import event.NomadRealmsGameEvent;
import networking.protocols.NomadRealmsP2PNetworkProtocol;

public abstract class NomadRealmsP2PNetworkEvent extends NomadRealmsGameEvent implements Derealizable {

	private NetworkSource source;

	public NetworkSource source() {
		return source;
	}

	public void setSource(NetworkSource source) {
		this.source = source;
	}

	public PacketModel toPacketModel(PacketAddress address) {
		byte[] serialized = serialize();
		short protocolID = formatEnum().id();
		byte[] bytes = new byte[serialized.length + 2];
		System.arraycopy(serialized, 0, bytes, 2, serialized.length);
		bytes[0] = (byte) ((protocolID >> 8) & 0xFF);
		bytes[1] = (byte) (protocolID & 0xFF);
		return new PacketModel(bytes, address);
	}

	@Override
	public abstract NomadRealmsP2PNetworkProtocol formatEnum();

}
