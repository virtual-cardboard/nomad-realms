package networking.protocols;

import derealizer.Derealizable;
import derealizer.DerealizerEnum;
import event.network.p2p.game.CardHoveredNetworkEvent;
import event.network.p2p.game.CardPlayedNetworkEvent;
import event.network.p2p.game.StreamChunkDataEvent;
import event.network.p2p.peerconnect.PeerConnectConfirmationEvent;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.peerconnect.PeerConnectResponseEvent;
import event.network.p2p.s2c.JoiningPlayerNetworkEvent;
import event.network.p2p.time.TimeRequestEvent;
import event.network.p2p.time.TimeResponseEvent;

public enum NomadRealmsP2PNetworkEventEnum implements DerealizerEnum {

	TIME_REQUEST_EVENT(TimeRequestEvent.class),
	TIME_RESPONSE_EVENT(TimeResponseEvent.class),
	PEER_CONNECT_REQUEST_EVENT(PeerConnectRequestEvent.class),
	PEER_CONNECT_RESPONSE_EVENT(PeerConnectResponseEvent.class),
	PEER_CONNECT_CONFIRMATION_EVENT(PeerConnectConfirmationEvent.class),
	STREAM_CHUNK_DATA_EVENT(StreamChunkDataEvent.class),
	CARD_PLAYED_NETWORK_EVENT(CardPlayedNetworkEvent.class),
	CARD_HOVERED_NETWORK_EVENT(CardHoveredNetworkEvent.class),
	JOINING_PLAYER_NETWORK_EVENT(JoiningPlayerNetworkEvent.class),
	;

	private final Class<? extends Derealizable> objClass;
	private final Class<? extends DerealizerEnum> derealizerEnum;

	NomadRealmsP2PNetworkEventEnum(Class<? extends Derealizable> objClass) {
		this(objClass, null);
	}

	NomadRealmsP2PNetworkEventEnum(Class<? extends Derealizable> objClass, Class<? extends DerealizerEnum> derealizerEnum) {
		this.objClass = objClass;
		this.derealizerEnum = derealizerEnum;
	}

	@Override
	public Class<? extends Derealizable> objClass() {
		return objClass;
	}

	@Override
	public Class<? extends DerealizerEnum> derealizerEnum() {
		return derealizerEnum;
	}

}
