package event.network;

import derealizer.Derealizable;
import derealizer.Derealizer;
import engine.common.networking.packet.PacketModel;
import engine.common.networking.packet.address.PacketAddress;
import engine.common.source.NetworkSource;
import event.NomadRealmsGameEvent;
import networking.protocols.NomadRealmsP2PNetworkEventEnum;

public abstract class NomadRealmsP2PNetworkEvent extends NomadRealmsGameEvent implements Derealizable {

	private NetworkSource source;

	public NetworkSource source() {
		return source;
	}

	public void setSource(NetworkSource source) {
		this.source = source;
	}

	public PacketModel toPacketModel(PacketAddress address) {
		return new PacketModel(Derealizer.serialize(this, NomadRealmsP2PNetworkEventEnum.class), address);
	}

}
