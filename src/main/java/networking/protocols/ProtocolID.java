package networking.protocols;

import event.network.CardHoveredNetworkEvent;
import event.network.CardPlayedNetworkEvent;
import event.network.NomadRealmsNetworkEvent;
import event.network.bootstrap.BootstrapRequestEvent;
import event.network.bootstrap.BootstrapResponseEvent;
import event.network.peerconnect.PeerConnectRequestEvent;
import event.network.peerconnect.PeerConnectResponseEvent;

public enum ProtocolID {

//	WORLD,
//	BACKUP,
//	ACCOUNT,
	BOOTSTRAP_REQUEST(BootstrapRequestEvent.class, 100),
	BOOTSTRAP_RESPONSE(BootstrapResponseEvent.class, 101),
	PEER_CONNECT_REQUEST(PeerConnectRequestEvent.class, 120),
	PEER_CONNECT_RESPONSE(PeerConnectResponseEvent.class, 121),
	CARD_PLAYED(CardPlayedNetworkEvent.class, 150),
	CARD_HOVERED(CardHoveredNetworkEvent.class, 150);
//	TRANSACTION,
//	NEWS,

	private static final ProtocolID[] IDS = new ProtocolID[Short.MAX_VALUE];

	static {
		ProtocolID[] values = ProtocolID.values();
		for (short i = 0; i < values.length; i++) {
			ProtocolID value = values[i];
			IDS[value.id] = value;
		}
	}

	private short id;
	private Class<? extends NomadRealmsNetworkEvent> clazz;

	private ProtocolID(Class<? extends NomadRealmsNetworkEvent> clazz, int id) {
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
