package model.actor;

import static derealizer.SerializationClassGenerator.generate;
import static derealizer.datatype.SerializationDataType.INT;
import static derealizer.format.SerializationFormat.types;

import derealizer.format.FieldNames;
import derealizer.format.Serializable;
import derealizer.format.SerializationFormat;
import derealizer.format.SerializationFormatEnum;
import model.GameObject;

public enum ActorSerializationFormats implements SerializationFormatEnum {
// Do not edit auto-generated code below this line.

	@FieldNames({ "health", "maxHealth" })
	HEALTH_ACTOR(types(INT, INT), HealthActor.class),
	@FieldNames({})
	EVENT_EMITTER_ACTOR(types(), EventEmitterActor.class), // TODO: Figure out what fields event emitter actor needs
	@FieldNames({})
	CARD_PLAYER(types(), CardPlayer.class), // TODO: Figure out how to serialize CardDashboard and Inventory
	;

	private final SerializationFormat format;
	private final Class<? extends Serializable> pojoClass;

	private ActorSerializationFormats(SerializationFormat format, Class<? extends Serializable> pojoClass) {
		this.format = format;
		this.pojoClass = pojoClass;
	}

	@Override
	public SerializationFormat format() {
		return format;
	}

	@Override
	public Class<? extends Serializable> pojoClass() {
		return pojoClass;
	}

	public static void main(String[] args) {
		generate(ActorSerializationFormats.class, GameObject.class);
	}

}
