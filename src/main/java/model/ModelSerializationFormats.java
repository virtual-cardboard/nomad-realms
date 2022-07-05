package model;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.BOOLEAN;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.datatype.SerializationDataType.STRING_UTF8;
import static derealizer.datatype.SerializationDataType.pojo;
import static derealizer.format.SerializationFormat.types;
import static engine.common.networking.packet.NetworkingSerializationFormats.PACKET_ADDRESS;
import static math.NomadRealmsMathSerializationFormats.WORLD_POS;

import derealizer.format.FieldNames;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import derealizer.format.SerializationPojo;
import model.actor.Actor;

public enum ModelSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "id" })
	GAME_OBJECT(types(LONG), GameObject.class),
	@FieldNames({ "worldPos", "shouldRemove" })
	ACTOR(types(pojo(WORLD_POS), BOOLEAN), Actor.class),

	@FieldNames({ "uuid", "username" })
	PLAYER(types(LONG, STRING_UTF8), null),
	@FieldNames({ "player", "lanAddress", "wanAddress" })
	PLAYER_SESSION(types(pojo(PLAYER), pojo(PACKET_ADDRESS), pojo(PACKET_ADDRESS)), null),
	;

	// Do not edit auto-generated code below this line.

	private final SerializationFormat format;
	private final Class<? extends SerializationPojo<?>> pojoClass;

	private ModelSerializationFormats(SerializationFormat format, Class<? extends SerializationPojo<?>> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends SerializationPojo<?>> pojoClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(ModelSerializationFormats.class, SerializationPojo.class);
	}

}
