package networking.protocols;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.datatype.SerializationDataType.STRING_UTF8;
import static derealizer.datatype.SerializationDataType.pojo;
import static derealizer.datatype.SerializationDataType.repeated;
import static derealizer.format.SerializationFormat.types;
import static engine.common.networking.packet.NetworkingSerializationFormats.PACKET_ADDRESS;

import derealizer.format.FieldNames;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import event.network.NomadRealmsC2SNetworkEvent;
import event.network.c2s.CreateWorldRequestEvent;
import event.network.c2s.DeleteWorldRequestEvent;
import event.network.c2s.JoinClusterRequestEvent;
import event.network.c2s.JoinClusterResponseEvent;
import event.network.c2s.JoinClusterSuccessEvent;

public enum NomadRealmsC2SNetworkProtocol implements SerializationFormatEnum {

	@FieldNames({ "lanAddress", "worldID", "playerId" })
	JOIN_CLUSTER_REQUEST_EVENT(types(pojo(PACKET_ADDRESS), LONG, LONG), JoinClusterRequestEvent.class),
	@FieldNames({
			"spawnTime", "spawnTick", "nonce", "username",
			"lanAddresses", "wanAddresses",
			"spawnPos"
	})
	JOIN_CLUSTER_RESPONSE_EVENT(
			types(
					LONG, LONG, LONG, STRING_UTF8,
					repeated(pojo(PACKET_ADDRESS)), repeated(pojo(PACKET_ADDRESS)),
					LONG
			),
			JoinClusterResponseEvent.class),
	@FieldNames({})
	JOIN_CLUSTER_SUCCESS_EVENT(types(), JoinClusterSuccessEvent.class),
	@FieldNames({ "seed", "worldName" })
	CREATE_WORLD_REQUEST_EVENT(types(LONG, STRING_UTF8), CreateWorldRequestEvent.class),
	@FieldNames({ "worldID", "worldName" })
	DELETE_WORLD_REQUEST_EVENT(types(LONG, STRING_UTF8), DeleteWorldRequestEvent.class),
	;

	// Do not edit auto-generated code below this line.

	private final SerializationFormat format;
	private final Class<? extends NomadRealmsC2SNetworkEvent> pojoClass;

	private NomadRealmsC2SNetworkProtocol(SerializationFormat format, Class<? extends NomadRealmsC2SNetworkEvent> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends NomadRealmsC2SNetworkEvent> pojoClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(NomadRealmsC2SNetworkProtocol.class, NomadRealmsC2SNetworkEvent.class);
	}

}
