package networking.protocols;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.datatype.SerializationDataType.STRING_UTF8;
import static derealizer.datatype.SerializationDataType.pojo;
import static derealizer.datatype.SerializationDataType.repeated;
import static derealizer.format.SerializationFormat.types;
import static engine.common.networking.packet.NetworkingSerializationFormats.PACKET_ADDRESS;
import static math.NomadRealmsMathSerializationFormats.WORLD_POS;
import static model.ModelSerializationFormats.ACTOR;
import static model.world.WorldSerializationFormats.TILE_CHUNK;

import derealizer.format.FieldNames;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import event.network.NomadRealmsP2PNetworkEvent;
import event.network.p2p.game.CardHoveredNetworkEvent;
import event.network.p2p.game.CardPlayedNetworkEvent;
import event.network.p2p.game.StreamChunkDataEvent;
import event.network.p2p.peerconnect.PeerConnectConfirmationEvent;
import event.network.p2p.peerconnect.PeerConnectRequestEvent;
import event.network.p2p.peerconnect.PeerConnectResponseEvent;
import event.network.p2p.s2c.JoiningPlayerNetworkEvent;
import event.network.p2p.time.TimeRequestEvent;
import event.network.p2p.time.TimeResponseEvent;

public enum NomadRealmsP2PNetworkProtocol implements SerializationFormatEnum {

	@FieldNames({})
	TIME_REQUEST_EVENT(types(), TimeRequestEvent.class),
	@FieldNames({ "receiveTime", "sendTime" })
	TIME_RESPONSE_EVENT(types(LONG, LONG), TimeResponseEvent.class),

	@FieldNames({ "nonce", "username" })
	PEER_CONNECT_REQUEST_EVENT(types(LONG, STRING_UTF8), PeerConnectRequestEvent.class),
	@FieldNames({ "nonce", "username" })
	PEER_CONNECT_RESPONSE_EVENT(types(LONG, STRING_UTF8), PeerConnectResponseEvent.class),
	@FieldNames({ "spawnPos" })
	PEER_CONNECT_CONFIRMATION_EVENT(types(pojo(WORLD_POS)), PeerConnectConfirmationEvent.class),

	@FieldNames({ "chunk", "actors" })
	STREAM_CHUNK_DATA_EVENT(types(pojo(TILE_CHUNK), repeated(pojo(ACTOR))), StreamChunkDataEvent.class),

	@FieldNames({ "playerId", "targetId", "cardId" })
	CARD_PLAYED_NETWORK_EVENT(types(LONG, LONG, LONG), CardPlayedNetworkEvent.class),
	@FieldNames({ "playerId", "cardId" })
	CARD_HOVERED_NETWORK_EVENT(types(LONG, LONG), CardHoveredNetworkEvent.class),

	@FieldNames({ "spawnTick", "nonce", "lanAddress", "wanAddress", "spawnPos" })
	JOINING_PLAYER_NETWORK_EVENT(types(LONG, LONG, pojo(PACKET_ADDRESS), pojo(PACKET_ADDRESS), pojo(WORLD_POS)), JoiningPlayerNetworkEvent.class),
	;

	// Set the id values for the protocol events.
	// All declarations above must have their id set in this static block.
	static {
		TIME_REQUEST_EVENT.id = 100;
		TIME_RESPONSE_EVENT.id = 101;

		PEER_CONNECT_REQUEST_EVENT.id = 120;
		PEER_CONNECT_RESPONSE_EVENT.id = 121;
		PEER_CONNECT_CONFIRMATION_EVENT.id = 122;

		STREAM_CHUNK_DATA_EVENT.id = 130;

		CARD_PLAYED_NETWORK_EVENT.id = 150;
		CARD_HOVERED_NETWORK_EVENT.id = 151;

		JOINING_PLAYER_NETWORK_EVENT.id = 200;
	}

	/**
	 * A number at the start of packets to indicate what type of packet it is
	 */
	private short id = -1;
	private final SerializationFormat format;
	private final Class<? extends NomadRealmsP2PNetworkEvent> pojoClass;

	private NomadRealmsP2PNetworkProtocol(SerializationFormat format, Class<? extends NomadRealmsP2PNetworkEvent> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	public short id() {
		return id;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends NomadRealmsP2PNetworkEvent> pojoClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(NomadRealmsP2PNetworkProtocol.class, NomadRealmsP2PNetworkEvent.class);
	}

}
