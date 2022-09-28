package model;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.BOOLEAN;
import static derealizer.datatype.SerializationDataType.LONG;
import static derealizer.datatype.SerializationDataType.STRING_UTF8;
import static derealizer.datatype.SerializationDataType.pojo;
import static derealizer.datatype.SerializationDataType.repeated;
import static derealizer.format.SerializationFormat.types;
import static engine.common.networking.packet.NetworkingSerializationFormats.PACKET_ADDRESS;
import static math.NomadRealmsMathSerializationFormats.WORLD_POS;
import static model.card.CardSerializationFormats.WORLD_CARD;

import derealizer.format.Derealizable;
import derealizer.format.FieldNames;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import model.actor.Actor;
import model.state.GameState;

public enum ModelSerializationFormats implements SerializationFormatEnum {

	@FieldNames({ "id" })
	GAME_OBJECT(types(LONG), GameObject.class),
	@FieldNames({ "worldPos", "shouldRemove" })
	ACTOR(types(pojo(WORLD_POS), BOOLEAN), Actor.class),

	// TODO: Serialize task
//	@FieldNames({})
//	TASK(types(), null),

	// TODO: tasks and hidden game objects
	@FieldNames({ "cards", "actors" })
	GAME_STATE(types(repeated(WORLD_CARD), repeated(ACTOR)), GameState.class),

	@FieldNames({ "uuid", "username" })
	PLAYER(types(LONG, STRING_UTF8), null),
	@FieldNames({ "player", "lanAddress", "wanAddress" })
	PLAYER_SESSION(types(pojo(PLAYER), pojo(PACKET_ADDRESS), pojo(PACKET_ADDRESS)), null),
	;

	private final SerializationFormat format;
	private final Class<? extends Derealizable> pojoClass;

	private ModelSerializationFormats(SerializationFormat format, Class<? extends Derealizable> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends Derealizable> objClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(ModelSerializationFormats.class, Derealizable.class);
	}

}
