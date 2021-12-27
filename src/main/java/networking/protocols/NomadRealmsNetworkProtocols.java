package networking.protocols;

import event.network.CardHoveredNetworkEvent;
import event.network.CardPlayedNetworkEvent;
import event.network.NomadRealmsNetworkEvent;
import event.network.bootstrap.BootstrapRequestEvent;
import event.network.bootstrap.BootstrapResponseEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import event.network.peerconnect.PeerConnectResponseEvent;
import event.network.world.CreateWorldRequestEvent;
import event.network.world.CreateWorldResponseEvent;

public enum NomadRealmsNetworkProtocols {

//	WORLD,
//	BACKUP,
//	ACCOUNT,
	BOOTSTRAP_REQUEST(BootstrapRequestEvent.class, 100),
	BOOTSTRAP_RESPONSE(BootstrapResponseEvent.class, 101),
	CREATE_WORLD_REQUEST(CreateWorldRequestEvent.class, 110),
	CREATE_WORLD_RESPONSE(CreateWorldResponseEvent.class, 111),
	PEER_CONNECT_REQUEST(PeerConnectRequestEvent.class, 120),
	PEER_CONNECT_RESPONSE(PeerConnectResponseEvent.class, 121),
	CARD_PLAYED(CardPlayedNetworkEvent.class, 150),
	CARD_HOVERED(CardHoveredNetworkEvent.class, 151);
//	TRANSACTION,
//	NEWS,

	private short id;
	private Class<? extends NomadRealmsNetworkEvent> clazz;

	private NomadRealmsNetworkProtocols(Class<? extends NomadRealmsNetworkEvent> clazz, int id) {
		this.clazz = clazz;
		this.id = (short) id;
	}

	public short id() {
		return id;
	}

	public Class<? extends NomadRealmsNetworkEvent> clazz() {
		return clazz;
	}

}